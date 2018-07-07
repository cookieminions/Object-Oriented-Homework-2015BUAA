package taxi;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

/* Overview: 这个类表示了运行的新型出租车,能够使出租车在地图中运行 保存了出租车的位置,状态,信用,id,响应的请求等信息 包括了一系列运动的方法 提供了一系列查询和设置出租车部分状态的方法
 * Objects: reqIterator, reqItr, cityMap, startTime, id, gui, RouteGuider, gotReqs, status, credit, waitTime, record, position, nextPoint;
 * newtaxi重写了父类taxi的运动方法和findroute方法 使得新型出租车能够运动在关闭的边上 不管是随机运动还是运动到目的地
 * newtaxi通过iterator方法返回迭代器
 * invariant: (super.repOk())&&(reqItr!=null)
 */
public class newtaxi extends taxi{
	String reqIterator;
	requestIterator reqItr;
	
	public boolean repOK(){
		/*
		  @MODIFIES: None;
		  @EFFECTS: (!super.repOk()) ==> \result == false;
		  			(reqItr==null) ==> \result == false;
		  			(其他情况) ==> \result == true;
		  */
		if(!super.repOK())	return false;
		if(reqItr==null)	return false;
		return true;
	}
	
	public newtaxi(point[][] map,long st,int d,TaxiGUI g){
		/*
		  @MODIFIES: reqIterator, reqItr, cityMap, startTime, id, gui, RouteGuider, gotReqs, status, credit, waitTime, record, position, nextPoint;
		  */
		super(map, st, d, g);
		gui.SetTaxiType(id, 1);
		reqIterator = "";
		reqItr = new requestIterator();
	}
	
	public TwoWayIterator iterator(){
		/*
		  @MODIFIES: None;
		  @EFFECTS: \result == reqItr;
		  */
		return reqItr;
	}
	
	public void desMove2C() {
		/*
		  @MODIFIES: waitTime, record, position, nextPoint, gui, gotReqs, status, reqIterator; 
		  @EFFECTS: update taxi's location when moving to customer;
		  */
		waitTime = 0;	record = "MoveToC: ";
		request req = this.getTaxiReq();
		momReq = req;
		reqIterator = momReq.reqInfor();
		reqIterator += "Taxi-"+id+" Get Req point: "+position.toString()+" MoveToC: ";
		Vector<point> route2C = RouteGuider.findroute(this.getPosition(), req.start_point, 1);
		if(route2C!=null){
		while(route2C.size()!=0){
			point nextP = route2C.remove(0);
			if(!this.getPosition().equals(nextP)){
				if(judgeWait(nextP)){
					waitLights("moving");
				}
				record += this.getPosition().toString()+"  ";
				reqIterator += this.getPosition().toString()+"  ";
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
		  @MODIFIES: waitTime, record, position, nextPoint, gui, status, credit, reqIterator, reqItr; 
		  @EFFECTS: update taxi's location when moving to destination;
		  */
		waitTime = 0;	record += "MoveToD: ";	reqIterator += "MoveToD: ";
		Vector<point> route2D = RouteGuider.findroute(this.getPosition(), momReq.end_point, 1);
		gui.RequestTaxi(new Point(momReq.start_point.x, momReq.start_point.y), new Point(momReq.end_point.x, momReq.end_point.y));
		if(route2D!=null){
		while(route2D.size()!=0){
			point nextP = route2D.remove(0);
			if(!this.getPosition().equals(nextP)){
				if(judgeWait(nextP)){
					waitLights("serving");
				}
				record += this.getPosition().toString()+"  ";
				reqIterator += this.getPosition().toString()+"  ";
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
		reqIterator += this.getPosition().toString()+"  ";
		reqItr.add(reqIterator);
		
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
			if(tmpFlow==666666) tmpFlow = 0;
			if(tmpFlow<minflow){
				if(Flows.size()!=0)	Flows.removeAllElements();
				Flows.add(i);
				minflow = tmpFlow;
			}else if(tmpFlow==minflow){
				Flows.add(i);
			}
		}
		int nextI = new Random().nextInt(Flows.size());
		point nextP = this.getPosition().getMinFlow(Flows.get(nextI),0);
		
		if(nextP==null){
			//System.out.println("ohoh");
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
	
	public Vector<point> findroute(point a,point b){
		/*@REQUIRES: None;
		  @MODIFIES: None;
		  @EFFECTS: \result == RouteGuider.findroute(a,b,1);
		  */
		return RouteGuider.findroute(a, b, 1);
	}
}

class requestIterator implements TwoWayIterator{
	Vector<String> reqList;
	int iterator;
	public boolean repOK(){
		/* 
		  @MODIFIES:None; 
		  @EFFECTS: (reqList==null) ==> \result == false;
		  			(其他情况) ==> \result == true;
		  */
		if(reqList==null)	return false;
		return true;
	}
	public requestIterator() {
		/*@REQUIRES: None;
		  @MODIFIES: reqList, iterator;
		  */
		reqList = new Vector<String>();
		iterator = 0;
	}
	public synchronized void add(String str){
		/*@REQUIRES: None;
		  @MODIFIES: reqList;
		  @THREAD_EFFECTS: \locked()
		  */
		reqList.add(str);
	}
	
	public synchronized Object next(){
		/*
		  @MODIFIES: iterator; 
		  @EFFECTS: (hasNext()) ==> \result == (Object)reqList.get(iterator) && iterator==\old(iterator)+1;
		  			(!hasNext()) ==> \result == null;
		  @THREAD_EFFECTS: \locked() 
		  */
		String nexts = null;
		if(hasNext()){
			try {
				nexts = reqList.get(iterator++);
				return (Object)nexts;
			} catch (Exception e) {
				return null;
			}
		}else{
			System.out.println("No Next");
		}
		return (Object)nexts;
	}
	public synchronized Object previous(){
		/*
		  @MODIFIES: iterator
		  @EFFECTS: (hasPrevious()) ==> \result == (Object)reqList.get(iterator-1) && iterator==\old(iterator)-1;
		  			(!hasPrevious()) ==> \result == null;
		  @THREAD_EFFECTS: \locked()
		  */
		String nexts = null;
		if(hasPrevious()){
			try {
				nexts = reqList.get(--iterator);
				return (Object)nexts;
			} catch (Exception e) {
				return null;
			}
		}else{
			System.out.println("No Previous");
		}
		return (Object)nexts;
	}
	public synchronized boolean hasNext(){
		/*
		  @MODIFIES: None;
		  @EFFECTS: (iterator+1<reqList.size()) ==> \result == true;
		  			(iterator+1>=reqList.size()) ==> \result == false;
		  @THREAD_EFFECTS: \locked()
		  */
		if(iterator<reqList.size()){
			return true;
		}else	return false;
	}
	public synchronized boolean hasPrevious(){
		/*
		  @MODIFIES: None;
		  @EFFECTS: (iterator-1>=0) ==> \result == true;
		  			(iterator<=0) ==> \result == false;
		  @THREAD_EFFECTS: \locked()
		  */
		if(iterator>0){
			return true;
		}else 	return false;
	}
}



