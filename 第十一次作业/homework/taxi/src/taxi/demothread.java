package taxi;

/* Overview: 供测试者测试的线程
 */
public class demothread extends Thread{
	private requestList reqList;
	private Taxi_Sys taxi_Sys;
	private CityMap citymap;
	private taxi[] taxis;
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
		taxis = taxi_Sys.taxis;
	}
	
	public void run(){
		/* 
		  @MODIFIES: None; 
		  */
		try {
			sleep(1000);
		} catch (InterruptedException e) {}
		citymap.setOpen(0, 0, 1, 0);
		citymap.setClose(4, 0, 4, 1);
		citymap.setClose(5, 0, 5, 1);
		reqList.submitReq("[CR,(4,2),(4,0)]");
		
		reqList.submitReq("[CR,(-10,20),(50,20)]");
		
		reqList.submitReq("[CR,(10,20),(20,50)]");
		reqList.submitReq("[CR,(10,20),(50,20)]");
		reqList.submitReq("[CR,(+10,20),(50,20)]");
		
		reqList.submitReq("(_)");
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {}
		
		reqList.submitReq("[CR,(10,5),(5,20)]");
		reqList.submitReq("[CR,(10,5),(5,21)]");
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {}
		
		System.out.println(taxi_Sys.getTaxisAtStatus("serving"));
		System.out.println(taxi_Sys.getTaxiInfor(10));

		reqList.submitReq("[CR,(4,0),(10,11)]");
		reqList.submitReq("[CR,(4,0),(10,10)]");
		reqList.submitReq("[CR,(4,0),(10,12)]");
		reqList.submitReq("[CR,(4,0),(10,14)]");
		
		try {
			sleep(60000);
		} catch (InterruptedException e) {}
		//迭代器的使用
		TwoWayIterator itr = taxis[3].iterator();
		while(itr.hasNext()){
			System.out.println((String)itr.next());
		}
		itr.next();
		System.out.println("--------------------------------");
		while(itr.hasPrevious()){
			System.out.println((String)itr.previous());
		}
		
	}
	
}
