package taxi;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

/* Overview: 这个类表示的是运行的出租车,能够使出租车在地图中运行 保存了出租车的位置,状态,信用,id,响应的请求等信息 包括了一系列运动的方法 提供了一系列查询和设置出租车部分状态的方法
 * Objects: cityMap, startTime, id, gui, RouteGuider, gotReqs, status, credit, waitTime, record, position, nextPoint;
 * taxi类的desMove2C和desMove2D方法使得出租车在接单后能够运行到乘客和目的地；randomMove方法使得出租车能够在等待状态下随机行走
 * taxi类的waitLights方法等待红绿灯
 * invariant: !((position==null||nextPoint==null||lastPoint==null||status==null||cityMap==null||
 * 				RouteGuider==null||gotReqs==null||record==null||gui==null)||
 * 				(cityMap.length!=80)||(\exist int i;0<=i<80;cityMap[i].length!=80)||
 * 				(\exist int i,j;0<=i<80,0<=j<80,;cityMap[i][j]==null)||
 * 				(\exist int i;0<=i<gotReqs.size();(req==null||!(req instanceof request))))
 */
public class taxi extends Thread{
	point position;
	point nextPoint;
	point lastPoint;
	String status;
	int credit;
	int id;
	
	point[][] cityMap;
	RouteBFS RouteGuider;
	Vector<request> gotReqs;
	
	String record;
	request momReq;
	int waitTime;
	long startTime;
	
	TaxiGUI gui;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (position==null||nextPoint==null||lastPoint==null||status==null||cityMap==null||RouteGuider==null||gotReqs==null||record==null||gui==null) ==> \result == false;
		  			(cityMap.length!=80) ==> \result == false;
		  			(\exist int i;0<=i<80;cityMap[i].length!=80) ==> \result == false;
		  			(\exist int i,j;0<=i<80,0<=j<80,;cityMap[i][j]==null) ==> \result == false;
		  			(\exist int i;0<=i<gotReqs.size();(req==null||!(req instanceof request))) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(position==null||nextPoint==null||lastPoint==null||status==null||cityMap==null
				||RouteGuider==null||gotReqs==null||record==null||gui==null)
			return false;
		if(cityMap.length!=80)	return false;
		for(int i=0;i<80;i++){
			if(cityMap[i].length!=80)	return false;
			for(int j=0;j<80;j++){
				if(cityMap[i][j]==null)	return false;
			}
		}
		for(int i=0;i<gotReqs.size();i++){
			Object req = gotReqs.get(i);
			if(req==null||!(req instanceof request))
				return false;
		}
		return true;
	}
	
	public TwoWayIterator iterator(){
		/*
		  @MODIFIES: None;
		  @EFFECTS: \result == new requestIterator();
		  */
		return new requestIterator();
	}
	
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
		//if(id ==3)	position = cityMap[4][0];
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		gui.SetTaxiType(id, 0);
		//System.out.println("taxi-"+id+" start at ("+position.x+","+position.y+")");
		nextPoint = position;
		lastPoint = position;
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
		  @EFFECTS: update taxi's location when moving to customer;
		  */
		waitTime = 0;	record = "MoveToC: ";
		request req = this.getTaxiReq();
		momReq = req;
		Vector<point> route2C = RouteGuider.findroute(this.getPosition(), req.start_point, 0);
		if(route2C!=null){
		while(route2C.size()!=0){
			point nextP = route2C.remove(0);
			if(!this.getPosition().equals(nextP)){
				if(this.getPosition().link.indexOf(nextP)==-1){
					route2C = RouteGuider.findroute(this.getPosition(), req.start_point, 0);
					continue;
				}
				if(judgeWait(nextP)){
					waitLights("moving");
				}
				
				record += this.getPosition().toString()+"  ";
				this.setNextPoint(nextP);
				try {
					sleep(200);
				} catch (InterruptedException e) {}
				position.calFlow(nextP);
				lastPoint = position;
				this.setPosition(nextP);
				gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
			}
		}
		carStop();
		this.setStatus("serving");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		}
	}
	
	public void desMove2D() {
		/*
		  @MODIFIES: waitTime, record, position, nextPoint, gui, status, credit; 
		  @EFFECTS: update taxi's location when moving to destination;
		  */
		waitTime = 0;	record += "MoveToD: ";
		Vector<point> route2D = RouteGuider.findroute(this.getPosition(), momReq.end_point, 0);
		gui.RequestTaxi(new Point(momReq.start_point.x, momReq.start_point.y), new Point(momReq.end_point.x, momReq.end_point.y));
		if(route2D!=null){
		while(route2D.size()!=0){
			point nextP = route2D.remove(0);
			
			if(!this.getPosition().equals(nextP)){
				if(!this.getPosition().link.contains(nextP)){
					route2D = RouteGuider.findroute(this.getPosition(), momReq.end_point, 0);
					continue;
				}
				if(judgeWait(nextP)){
					waitLights("serving");
				}
				
				record += this.getPosition().toString()+"  ";
				this.setNextPoint(nextP);
				try {
					sleep(200);
				} catch (InterruptedException e) {}
				position.calFlow(nextP);
				lastPoint = position;
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
		  @EFFECTS: update taxi's location randomly;
		  */
		Vector<Integer> Flows = new Vector<Integer>();
		int minflow = 999999;
		for(int i=0;i<4;i++){
			int tmpFlow = this.getPosition().nflow.get(i);
			if(tmpFlow<minflow){
				if(Flows.size()!=0)	Flows.removeAllElements();
				Flows.add(i);
				minflow = tmpFlow;
			}else if(tmpFlow==minflow){
				Flows.add(i);
			}
		}
		int nextI = new Random().nextInt(Flows.size());
		point nextP = this.getPosition().getMinFlow(Flows.get(nextI),1);
		
		if(nextP==null){
			System.out.println("point alone");
			if(this.getPosition().getLinkSize()==0){
				System.out.println("point alone");
				return;
			}
			return;
		}
		if(judgeWait(nextP)){
			waitLights("waiting");
		}
		
		this.setStatus("waiting");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		this.setNextPoint(nextP);
		waitTime ++;
		try {
			sleep(200);
		} catch (InterruptedException e) {}
		position.calFlow(nextP);
		lastPoint = position;
		this.setPosition(nextP);
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
	}
	
	public void carStop(){
		/*
		  @MODIFIES: gui, status, waitTime;
		  */
		this.setStatus("stop");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		waitTime = 0;
		try {
			sleep(1000);
		} catch (InterruptedException e) {}
		this.setStatus("waiting");
		//gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
	}
	
	public void waitLights(String st){
		/*
		  @MODIFIES: status, gui;
		  */
		int l = position.light.get();
		if(l==-1)	return;
		this.setStatus("stop");
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
		while(position.light.get()==l){
			try {
				sleep(4);
			} catch (InterruptedException e) {}
			yield();
		}
		this.setStatus(st);
		gui.SetTaxiStatus(id, new Point(position.x, position.y), calStatus());
	}
	
	public boolean judgeWait(point nextP){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (getDire(nextP)==0||getDire(nextP)==3||(getDire(nextP)==1 && position.light.get()==0)||(getDire(nextP)==2 && position.light.get()==0)) ==> \result == true;
		  			!(getDire(nextP)==0||getDire(nextP)==3||(getDire(nextP)==1 && position.light.get()==0)||(getDire(nextP)==2 && position.light.get()==0)) ==> \result == false;
		  */
		int dir = getDire(nextP);//上下为红则为0
		if((dir==0)||(dir==1&&position.light.get()==0)||(dir==2&&position.light.get()==1)||
		   (dir==3)||(dir==-1&&position.light.get()==1)||(dir==-2&&position.light.get()==0))
			return false;
		return true;
	}
	
	public int getDire(point nextP){
		/*
		  @MODIFIES: None;
		  @EFFECTS: ((lastPoint.y==position.y)&&((lastPoint.x-1)==position.x&&position.x==nextP.x&&(position.y+1)==nextP.y)||((lastPoint.x+1)==position.x&&position.x==nextP.x&&(position.y-1)==nextP.y))||
		  			((lastPoint.x==position.x)&&((lastPoint.y-1)==position.y&&position.y==nextP.y&&(position.x-1)==nextP.x)||((lastPoint.y+1)==position.y&&position.y==nextP.y&&(position.x+1)==nextP.x))
		  			==> \result == 0;
					((lastPoint.y==position.y)&&(((lastPoint.x-1)==position.x&&position.x==nextP.x&&(position.y-1)==nextP.y)||((lastPoint.x+1)==position.x&&position.x==nextP.x&&(position.y+1)==nextP.y))
		  			==> \result == 1;
		  			((lastPoint.x==position.x)&&(((lastPoint.y-1)==position.y&&position.y==nextP.y&&(position.x+1)==nextP.x)||((lastPoint.y+1)==position.y&&position.y==nextP.y&&(position.x-1)==nextP.x))
					==> \result == -1;
					((lastPoint.y==position.y)&&(((lastPoint.x-1)==position.x&&(position.x-1)==nextP.x&&position.y==nextP.y)||((lastPoint.x+1)==position.x&&(position.x+1)==nextP.x&&position.y==nextP.y))
		  			==> \result == 2;
		  			((lastPoint.x==position.x)&&(((lastPoint.y-1)==position.y&&position.y==nextP.y&&(position.x+1)==nextP.x)||((lastPoint.y+1)==position.y&&position.y==nextP.y&&(position.x-1)==nextP.x))
					==> \result == -2;
					((lastPoint.y==position.y)&&(((lastPoint.x-1)==position.x&&(position.x+1)==nextP.x&&position.y==nextP.y)||((lastPoint.x+1)==position.x&&(position.x-1)==nextP.x&&position.y==nextP.y))||
		  			((lastPoint.x==position.x)&&(((lastPoint.y-1)==position.y&&(position.y+1)==nextP.y&&position.x==nextP.x)||((lastPoint.y+1)==position.y&&(position.y-1)==nextP.y&&position.x==nextP.x))
		  			==> \result == 3;
					(其他情况成立) ==> \result == 0;
		  */
		if(lastPoint.y==position.y){
			if(((lastPoint.x-1)==position.x&&position.x==nextP.x&&(position.y+1)==nextP.y)||
					((lastPoint.x+1)==position.x&&position.x==nextP.x&&(position.y-1)==nextP.y)){
				return 0;//turn right
			}
			else if(((lastPoint.x-1)==position.x&&position.x==nextP.x&&(position.y-1)==nextP.y)||
					((lastPoint.x+1)==position.x&&position.x==nextP.x&&(position.y+1)==nextP.y)){
				return 1;//turn left
			}
			else if(((lastPoint.x-1)==position.x&&(position.x-1)==nextP.x&&position.y==nextP.y)||
					((lastPoint.x+1)==position.x&&(position.x+1)==nextP.x&&position.y==nextP.y)){
				return 2;//turn straight
			}
			else if(((lastPoint.x-1)==position.x&&(position.x+1)==nextP.x&&position.y==nextP.y)||
					((lastPoint.x+1)==position.x&&(position.x-1)==nextP.x&&position.y==nextP.y)){
				return 3;//turn back
			}
		}else if(lastPoint.x==position.x){
			if(((lastPoint.y-1)==position.y&&position.y==nextP.y&&(position.x-1)==nextP.x)||
					((lastPoint.y+1)==position.y&&position.y==nextP.y&&(position.x+1)==nextP.x)){
				return 0;//turn right
			}
			else if(((lastPoint.y-1)==position.y&&position.y==nextP.y&&(position.x+1)==nextP.x)||
					((lastPoint.y+1)==position.y&&position.y==nextP.y&&(position.x-1)==nextP.x)){
				return -1;//turn left
			}
			else if(((lastPoint.y-1)==position.y&&(position.y-1)==nextP.y&&position.x==nextP.x)||
					((lastPoint.y+1)==position.y&&(position.y+1)==nextP.y&&position.x==nextP.x)){
				return -2;//turn straight
			}
			else if(((lastPoint.y-1)==position.y&&(position.y+1)==nextP.y&&position.x==nextP.x)||
					((lastPoint.y+1)==position.y&&(position.y-1)==nextP.y&&position.x==nextP.x)){
				return 3;//turn back
			}
		}
		return 0;
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
		if(this.getStatus().equals("stop"))		return 0;
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
	
	private void writeFile(request req, String str){
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
	
	public Vector<point> findroute(point a,point b){
		/*@REQUIRES: None;
		  @MODIFIES: None;
		  @EFFECTS: \result == RouteGuider.findroute(a,b,0);
		  */
		return RouteGuider.findroute(a, b, 0);
	}
	
}
