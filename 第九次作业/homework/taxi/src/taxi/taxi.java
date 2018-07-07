package taxi;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

public class taxi extends Thread{
	private point position;
	private point nextPoint;
	private String status;
	private int credit;
	private int id;
	
	private point[][] cityMap;
	private RouteBFS RouteGuider;
	private Vector<request> gotReqs;
	
	private String record;
	private request momReq;
	private int waitTime;
	private long startTime;
	
	private TaxiGUI gui;
	
	public taxi(point[][] map,long st,int d,TaxiGUI g){
		/*
		  @MODIFIES: cityMap, startTime, id, gui, RouteGuider, gotReqs, status, credit, waitTime, record, position, nextPoint; 
		  */
		cityMap = map;
		startTime =st;
		id = d;
		gui = g;
		
		RouteGuider = new RouteBFS(cityMap);
		gotReqs = new Vector<request>();
		
		status = "waiting";
		credit = 0;
		waitTime = 0;
		record = "";
		
		Random r = new Random();
		position = cityMap[r.nextInt(80)][r.nextInt(80)];
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		//System.out.println("taxi-"+id+" start at ("+position.x+","+position.y+")");
		nextPoint = position;
	}
	
	public void run() {
		/* 
		  @MODIFIES: gui, status, credit, waitTime, record, position, nextPoint;
		  */
		while(true){
			if(this.getStatus().equals("waiting")){
				randomMove();
			}
			if(this.getStatus().equals("moving")){
				desMove2C();
			}
			if(this.getStatus().equals("serving")){
				desMove2D();
				writeFile(momReq, record+"\r\n");
			}
			if(waitTime==100){
				carStop();
			}
			yield();
		}
	}
	
	public void desMove2C() {
		/*
		  @MODIFIES: waitTime, record, position, nextPoint, gui, gotReqs, status; 
		  */
		waitTime = 0;	record = "MoveToC: ";
		request req = this.getTaxiReq();
		momReq = req;
		Vector<point> route2C = RouteGuider.findroute(this.getPosition(), req.start_point);
		if(route2C!=null){
		while(route2C.size()!=0){
			point nextP = route2C.remove(0);
			if(!this.getPosition().equals(nextP)){
				if(this.getPosition().link.indexOf(nextP)==-1){
					route2C = RouteGuider.findroute(this.getPosition(), req.start_point);
					continue;
				}
				
				record += this.getPosition().toString()+"  ";
				this.setNextPoint(nextP);
				try {
					sleep(200);
				} catch (InterruptedException e) {}
				position.calFlow(nextP);
				this.setPosition(nextP);
				gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
			}
		}
		this.setStatus("serving");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		}
	}
	
	public void desMove2D() {
		/*
		  @MODIFIES: waitTime, record, position, nextPoint, gui, status, credit; 
		  */
		waitTime = 0;	record += "MoveToD: ";
		Vector<point> route2D = RouteGuider.findroute(this.getPosition(), momReq.end_point);
		gui.RequestTaxi(new Point(momReq.start_point.x, momReq.start_point.y), new Point(momReq.end_point.x, momReq.end_point.y));
		if(route2D!=null){
		while(route2D.size()!=0){
			point nextP = route2D.remove(0);
			
			if(!this.getPosition().equals(nextP)){
				if(!this.getPosition().link.contains(nextP)){
					route2D = RouteGuider.findroute(this.getPosition(), momReq.end_point);
					continue;
				}
				record += this.getPosition().toString()+"  ";
				this.setNextPoint(nextP);
				try {
					sleep(200);
				} catch (InterruptedException e) {}
				position.calFlow(nextP);
				this.setPosition(nextP);
				gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
			}
		}
		record += this.getPosition().toString()+"  ";
		this.addCredit(3);
		carStop();
		}
		if(this.getReqLeft()!=0)	{this.setStatus("moving");	gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());}
	}
	
	public void randomMove(){
		/* 
		  @MODIFIES: waitTime, position, nextPoint, gui, status; 
		  */
		int minflow = 0;
		for(int i=0;i<4;i++){
			if(this.getPosition().getFlow(i)<this.getPosition().getFlow(minflow)){
				minflow = i;
			}
		}
		point nextP = this.getPosition().getMinFlow(minflow);
		if(nextP==null){
			if(this.getPosition().getLinkSize()==0){
				System.out.println("point alone");
				return;
			}
		}
		this.setStatus("waiting");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		this.setNextPoint(nextP);
		waitTime ++;
		try {
			sleep(200);
		} catch (InterruptedException e) {}
		position.calFlow(nextP);
		this.setPosition(nextP);
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
	}
	
	public void carStop(){
		/*
		  @MODIFIES: gui, status, waitTime;
		  */
		//gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		this.setStatus("stop");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		waitTime = 0;
		try {
			sleep(1000);
		} catch (InterruptedException e) {}
		this.setStatus("waiting");
		//gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
	}
	public synchronized int getTaxiId() {
		/* 
		  @EFFECTS: \result == id;
		  @THREAD_EFFECTS: \locked();
		  */
		return id;
	}
	public synchronized int calStatus(){
		/* 
		  @EFFECTS: (this.getStatus().equals("stop")) ==> \result == 0;
		  			(this.getStatus().equals("serving")) ==> \result == 1;
		  			(this.getStatus().equals("waiting")) ==> \result == 2;
		  			(this.getStatus().equals("moving")) ==> \result == 3;
		  @THREAD_EFFECTS: \locked();
		  */
		if(this.getStatus().equals("stop"))	return 0;
		if(this.getStatus().equals("serving"))	return 1;
		if(this.getStatus().equals("waiting"))	return 2;
		if(this.getStatus().equals("moving"))	return 3;
		return 0;
	}
	
	public synchronized point getPosition() {
		/* 
		  @EFFECTS: \result == position;
		  @THREAD_EFFECTS: \locked();
		  */
		return position;
	}
	public synchronized point getnextPoint() {
		/* 
		  @EFFECTS: \result == nextPosition;
		  @THREAD_EFFECTS: \locked();
		  */
		return nextPoint;
	}
	public synchronized int getCredit() {
		/* 
		  @EFFECTS: \result == credit;
		  @THREAD_EFFECTS: \locked();
		  */
		return credit;
	}
	public synchronized String getStatus() {
		/* 
		  @EFFECTS: \result == status;
		  @THREAD_EFFECTS: \locked();
		  */
		return status;
	}
	public synchronized void setStatus(String str){
		/* 
		  @MODIFIES: status;
		  @THREAD_EFFECTS: \locked();
		  */
		status = str;
	}
	public synchronized void setPosition(point t) {
		/* 
		  @MODIFIES: position;
		  @THREAD_EFFECTS: \locked();
		  */
		position = t;
	}
	public synchronized void setNextPoint(point t) {
		/* 
		  @MODIFIES: nextPoint;
		  @THREAD_EFFECTS: \locked();
		  */
		nextPoint = t;
	}
	public synchronized void addCredit(int i) {
		/* 
		  @MODIFIES: credit;
		  @THREAD_EFFECTS: \locked();
		  */
		credit += i;
	}
	
	public synchronized void addTaxiReq(request req){
		/* 
		  @MODIFIES: gotReqs;
		  @THREAD_EFFECTS: \locked();
		  */
		gotReqs.add(req);
	}
	public synchronized request getTaxiReq(){
		/* 
		  @MODIFIES: gotReqs;
		  @EFFECTS: \result == gotReqs.remove(0);
		  @THREAD_EFFECTS: \locked();
		  */
		return gotReqs.remove(0);
	}
	public synchronized int getReqLeft() {
		/* 
		  @EFFECTS: \result == gotReqs.size();
		  @THREAD_EFFECTS: \locked();
		  */
		return gotReqs.size();
	}
	
	public double getTime(){
		/* 
		  @MODIFIES: None;
		  @EFFECTS: \result == Double((System.currentTimeMillis()-startTime)/100);
		  */
		double t = (System.currentTimeMillis()-startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
	
	public void writeFile(request req, String str){
		/* 
		  @MODIFIES: None
		  */
		String path = req.toString()+".txt";
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file,true);
			fw.write(str);
			fw.close();
		} catch (IOException e) {}
	}
	
}
