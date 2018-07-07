package taxi;


public class demothread extends Thread{
	/* Overview: 供测试者测试的线程
	 */
	
	private requestList reqList;
	private Taxi_Sys taxi_Sys;
	private CityMap citymap;
	public boolean repOK() {
		/* 
		  @EFFECTS: (reqList==null||taxi_Sys==null||citymap==null) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(reqList==null||taxi_Sys==null||citymap==null)	return false;
		return true;
	}
	
	public demothread(requestList reqlist,Taxi_Sys Sys,CityMap map){
		/* 
		  @MODIFIES: reqList, taxi_Sys, citymap; 
		  */
		reqList = reqlist;
		taxi_Sys = Sys;
		citymap = map;
	}
	
	public void run(){
		/* 
		  @MODIFIES: None; 
		  */
		try {
			sleep(1000);
		} catch (InterruptedException e) {}
		
		reqList.submitReq("[CR,(-10,20),(50,20)]");
		
		reqList.submitReq("[CR,(10,20),(20,50)]");
		reqList.submitReq("[CR,(10,20),(50,20)]");
		reqList.submitReq("[CR,(+10,20),(50,20)]");
		
		reqList.submitReq("(_)");
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {}
		
		citymap.setClose(79, 78, 79, 79);
		
		citymap.setClose(0, 0, 0, 1);
		
		reqList.submitReq("[CR,(10,5),(5,20)]");
		reqList.submitReq("[CR,(10,5),(5,21)]");
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {}
		citymap.setOpen(0, 0, 0, 1);
		citymap.setOpen(0, 0, 0, 0);
		citymap.setClose(0, 0, 1, 1);
		
		System.out.println(taxi_Sys.getTaxisAtStatus("serving"));
		System.out.println(taxi_Sys.getTaxiInfor(10));
	}
	
}
