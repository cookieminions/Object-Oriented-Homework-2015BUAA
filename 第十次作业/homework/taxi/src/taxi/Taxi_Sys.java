package taxi;

import java.text.DecimalFormat;
import java.util.Vector;

public class Taxi_Sys extends Thread{
	/* Overview: 这个类能够从请求队列中取出新提交的请求 并根据请求开启抢单窗口分配出租车响应; 提供了查询出租车状态和查询指定状态出租车的方法
	 */
	
	private point[][] cityMap;
	private long startTime;
	private requestList reqList;
	private TaxiGUI taxiGUI;
	taxi[] taxis;
	
	RouteBFS RouteGuider;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (cityMap==null||reqList==null||taxiGUI==null||taxis==null||RouteGuider==null) ==> \result == false;
		  			(cityMap.length!=80||taxis.length!=100) ==> \result == false;
		  			(\exist int i;0<=i<80;cityMap[i].length!=80) ==> \result == false;
		  			(\exist int i,j;0<=i<80,0<=j<80,;cityMap[i][j]==null) ==> \result == false;
		  			(\exist int i;0<=i<100;taxis[i]==null) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(cityMap==null||reqList==null||taxiGUI==null||taxis==null||RouteGuider==null)
			return false;
		if(cityMap.length!=80||taxis.length!=100)	return false;
		for(int i=0;i<80;i++){
			if(cityMap[i].length!=80)	return false;
			for(int j=0;j<80;j++){
				if(cityMap[i][j]==null)	return false;
			}
		}
		for(int i=0;i<100;i++){
			if(taxis[i]==null)	return false;
		}
		return true;
	}
	
	public Taxi_Sys(point[][] map,requestList reqlist, long st, TaxiGUI gui) {
		/*
		  @MODIFIES: cityMap, reqList, startTime, taxis, RouteGuider, taxiGUI;
		  */
		cityMap = map;
		reqList = reqlist;
		startTime = st;
		taxis = new taxi[100];
		RouteGuider = new RouteBFS(cityMap);
		taxiGUI = gui;
	}
	public void run(){
		/*
		  @MODIFIES: reqList;
		  */
		for(int i=0;i<100;i++){
			taxis[i] = new taxi(cityMap,startTime,i,taxiGUI);
			taxis[i].start();
		}
		while(true){
			distr();
			yield();
		}
	}
	
	public void distr() {
		/*
		  @MODIFIES: reqList;
		  */
		Vector<request> getlist = reqList.getReqList();
		
		for(int i=0;i<getlist.size();i++){
			grabWindow window = new grabWindow(cityMap, taxis, getlist.get(i),startTime);
			window.start();
		}
		
	}
	
	public synchronized String getTaxiInfor(int i) {
		/*
		  @MODIFIES: None
		  @EFFECTS: \result == "taxi-"+i+" "+taxis[i].getStatus()+" "+taxis[i].getPosition().toString()+" currTime: "+getTime();
		  @THREAD_EFFECTS: \locked();
		  */
		String str = "taxi-"+i+" "+taxis[i].getStatus()+" "+taxis[i].getPosition().toString()+" currTime: "+getTime();
		return str;
	}
	
	public synchronized String getTaxisAtStatus(String status) {
		/*
		  @MODIFIES: None
		  @EFFECTS: (出租车中有处于 status状态的) ==> \result == (处于status状态的出租车编号拼接);
		  			(出租车中没有处于 status状态的) ==> \result == No Taxi at "+status;
		  @THREAD_EFFECTS: \locked();
		  */
		String str = "";
		//Vector<taxi> tmpTaxis = new Vector<taxi>();
		for(int i=0;i<100;i++){
			if(taxis[i].getStatus().equals(status)){
				str += "taxi-"+i+" ";
				//tmpTaxis.add(taxis[i]);
			}
		}
		if(str.equals(""))	str = "No Taxi at "+status;
		//return tmpTaxis;
		return str;
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
	
}
