package taxi;

import java.text.DecimalFormat;
import java.util.Vector;

public class requestList {
	/* Overview: 这个类存储了请求队列 并提供了提交请求和取出请求的方法
	 */
	
	private Vector<request> reqList;
	private ReqCheck reqCheck;
	private long startTime;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (reqList==null||reqCheck==null||!((Object)startTime instanceof Long)) ==> \result == false;
		  			(\exist int i;0<=i<reqList.size();(req==null||!(req instanceof request))) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(reqList==null||reqCheck==null||!((Object)startTime instanceof Long))
			return false;
		for(int i=0;i<reqList.size();i++){
			Object req = reqList.get(i);
			if(req==null||!(req instanceof request))
				return false;
		}
		return true;
	}
	
	public requestList(long startT) {
		/*
		  @MODIFIES: reqList, reqCheck, startTime;
		  */
		reqList = new Vector<request>();
		reqCheck = new ReqCheck();
		startTime = startT;
	}
	
	public synchronized Vector<request> getReqList(){
		/*
		  @MODIFIES: reqList;
		  @EFFECTS: tmpList == reqList.removeAll();
		  			\result == tmpList;
		  @THREAD_EFFECTS: locked();
		  */
		Vector<request> tmpReqList = new Vector<request>();
		while(reqList.size()!=0){
			tmpReqList.add(reqList.remove(0));
		}
		return tmpReqList;
	}
	
	public synchronized void addReq(request req) {
		/*
		  @MODIFIES: reqList;
		  @EFFECTS: (req出发地与目的地不同 && req不是相同请求) ==> reqList.add(req);
		  @THREAD_EFFECTS: locked();
		  */
		if(req.start_point.equals(req.end_point))	return;
		for(int i=0;i<reqList.size();i++){
			if(reqList.get(i).equals(req)){
				System.out.println("SAME REQ "+req.toString());
				return;
			}
		}
		reqList.add(req);
	}
	
	public synchronized void submitReq(String str){
		/*
		  @MODIFIES: reqList;
		  @EFFECTS: (reqCheck.checkReq(str, getTime())!=null) ==> reqList.add(reqCheck.checkReq(str, getTime()));
		  @THREAD_EFFECTS: locked();
		  */
		double t = getTime();
		request req = reqCheck.checkReq(str, t);
		if(req!=null){
			this.addReq(req);
		}else{
			System.out.println("INVALID REQ");
		}
	}
	
	public double getTime() {
		/* 
		  @MODIFIES: None;
		  @EFFECTS: \result == Double((System.currentTimeMillis()-startTime)/100);
		  */
		double t = (System.currentTimeMillis() - startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
}
