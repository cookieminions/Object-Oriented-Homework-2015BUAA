package taxi;

import java.text.DecimalFormat;
import java.util.Vector;

public class Taxi_Sys extends Thread{
	private point[][] cityMap;
	private taxi[] taxis;
	private long startTime;
	private requestList reqList;
	private TaxiGUI taxiGUI;
	RouteBFS RouteGuider;
	
	public Taxi_Sys(point[][] map,requestList reqlist, long st, TaxiGUI gui) {
		cityMap = map;
		reqList = reqlist;
		startTime = st;
		taxis = new taxi[100];
		RouteGuider = new RouteBFS(cityMap);
		taxiGUI = gui;
	}
	public void run(){
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
		Vector<request> getlist = reqList.getReqList();
		
		for(int i=0;i<getlist.size();i++){
			grabWindow window = new grabWindow(cityMap, taxis, getlist.get(i),startTime);
			window.start();
		}
		
	}
	
	public synchronized String getTaxiInfor(int i) {
		String str = "taxi-"+i+" "+taxis[i].getStatus()+" "+taxis[i].getPosition().toString()+" currTime: "+getTime();
		return str;
	}
	
	public synchronized String getTaxisAtStatus(String status) {
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
		double t = (System.currentTimeMillis()-startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
	
}
