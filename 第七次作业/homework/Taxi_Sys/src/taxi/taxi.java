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
	private Vector<Vector<point>> route2Ds;
	
	private String record;
	private request momReq;
	private int waitTime;
	private long startTime;
	
	private TaxiGUI gui;
	
	public taxi(point[][] map,long st,int d,TaxiGUI g){
		cityMap = map;
		startTime =st;
		id = d;
		gui = g;
		
		RouteGuider = new RouteBFS(cityMap);
		gotReqs = new Vector<request>();
		route2Ds = new Vector<Vector<point>>();
		
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
		waitTime = 0;	record = "MoveToC: ";
		request req = this.getTaxiReq();
		momReq = req;
		gui.RequestTaxi(new Point(req.start_point.x, req.start_point.y), new Point(req.end_point.x, req.end_point.y));
		Vector<point> route2C = RouteGuider.findroute(this.getPosition(), req.start_point);
		if(route2C!=null){
		while(route2C.size()!=0){
			point nextP = route2C.remove(0);
			if(!this.getPosition().equals(nextP)){
				record += this.getPosition().toString()+"  ";
				//System.out.println("taxi-"+id+" MoveToC ("+(this.getPosition().x)+","+(this.getPosition().y)+") "+getTime());
				this.setNextPoint(nextP);
				try {
					sleep(200);
				} catch (InterruptedException e) {}
				this.setPosition(nextP);
				gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
			}
		}
		this.setStatus("serving");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		}
	}
	
	public void desMove2D() {
		waitTime = 0;	record += "MoveToD: ";
		Vector<point> route2D = this.getRoute2D();
		//System.out.println(route2D.size()-1);
		int distance = route2D.size()>0?route2D.size()-1:0;
		record += "Distance: "+distance+" ";
		if(route2D!=null){
		while(route2D.size()!=0){
			point nextP = route2D.remove(0);
			if(!this.getPosition().equals(nextP)){
				record += this.getPosition().toString()+"  ";
				//System.out.println("taxi-"+id+" MoveToD ("+(this.getPosition().x)+","+(this.getPosition().y)+") "+getTime());
				this.setNextPoint(nextP);
				try {
					sleep(200);
				} catch (InterruptedException e) {}
				this.setPosition(nextP);
				gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
				//System.out.println(position.toString());
			}
		}
		record += this.getPosition().toString()+"  ";
		//System.out.println("taxi-"+id+" MoveToD ("+(this.getPosition().x)+","+(this.getPosition().y)+") "+getTime());
		//System.out.println("get destination");
		this.addCredit(3);
		carStop();
		}
		if(this.getReqLeft()!=0)	{this.setStatus("moving");	gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());}
	}
	
	public void randomMove(){
		Random random = new Random();
		int next = random.nextInt(position.link.size());
		point nextP = position.link.get(next);
		
		this.setStatus("waiting");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		this.setNextPoint(nextP);
		waitTime ++;
		try {
			sleep(200);
		} catch (InterruptedException e) {}
		this.setPosition(nextP);
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
	}
	
	public void carStop(){
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
		return id;
	}
	public synchronized int calStatus(){
		if(this.getStatus().equals("stop"))	return 0;
		if(this.getStatus().equals("serving"))	return 1;
		if(this.getStatus().equals("waiting"))	return 2;
		if(this.getStatus().equals("moving"))	return 3;
		return 0;
	}
	
	public synchronized point getPosition() {
		return position;
	}
	public synchronized point getnextPoint() {
		return nextPoint;
	}
	public synchronized int getCredit() {
		return credit;
	}
	public synchronized String getStatus() {
		return status;
	}
	public synchronized void setStatus(String str){
		status = str;
	}
	public synchronized void setPosition(point t) {
		position = t;
	}
	public synchronized void setNextPoint(point t) {
		nextPoint = t;
	}
	public synchronized void addCredit(int i) {
		credit += i;
	}
	
	public synchronized void addTaxiReq(request req){
		gotReqs.add(req);
	}
	public synchronized request getTaxiReq(){
		return gotReqs.remove(0);
	}
	public synchronized int getReqLeft() {
		return gotReqs.size();
	}
	public synchronized void setRoute2D(Vector<point> r){
		route2Ds.add(r);
	}
	public synchronized Vector<point> getRoute2D(){
		if(route2Ds.size()!=0){
			return route2Ds.remove(0);
		}
		return null;
	}
	public synchronized int getRouteSize() {
		return route2Ds.size();
	}
	public double getTime(){
		double t = (System.currentTimeMillis()-startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
	
	public void writeFile(request req, String str){
		String path = req.toString()+".txt";
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file,true);
			fw.write(str);
			fw.close();
		} catch (IOException e) {}
	}
	
}
