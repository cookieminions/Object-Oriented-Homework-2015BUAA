package taxi;

public class demothread extends Thread{
	private requestList reqList;
	private Taxi_Sys taxi_Sys;
	public demothread(requestList reqlist,Taxi_Sys Sys){
		reqList = reqlist;
		taxi_Sys = Sys;
	}
	
	public void run(){
		try {
			sleep(1000);
		} catch (InterruptedException e) {}
		
		reqList.submitReq("[CR,(10,20),(20,50)]");
		reqList.submitReq("[CR,(10,20),(50,20)]");
		reqList.submitReq("[CR,(10,20),(50,20)]");
		
		reqList.submitReq("(_)");
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {}
		
		reqList.submitReq("[CR,(10,5),(5,20)]");
		reqList.submitReq("[CR,(10,5),(5,21)]");
		
		
		System.out.println(taxi_Sys.getTaxisAtStatus("serving"));
		System.out.println(taxi_Sys.getTaxiInfor(10));
		
	}
	
}
