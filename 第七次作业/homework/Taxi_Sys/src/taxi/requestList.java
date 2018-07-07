package taxi;

import java.text.DecimalFormat;
import java.util.Vector;

public class requestList {
	private Vector<request> reqList;
	private ReqCheck reqCheck;
	private long startTime;
	
	public requestList(long startT) {
		reqList = new Vector<request>();
		reqCheck = new ReqCheck();
		startTime = startT;
	}
	
	public synchronized Vector<request> getReqList(){
		Vector<request> tmpReqList = new Vector<request>();
		while(reqList.size()!=0){
			tmpReqList.add(reqList.remove(0));
		}
		return tmpReqList;
	}
	
	public synchronized void addReq(request req) {
		for(int i=0;i<reqList.size();i++){
			if(reqList.get(i).equals(req)){
				System.out.println("SAME REQ "+req.toString());
				return;
			}
		}
		reqList.add(req);
	}
	
	public synchronized void submitReq(String str){
		double t = getTime();
		request req = reqCheck.checkReq(str, t);
		if(req!=null){
			this.addReq(req);
		}else{
			System.out.println("INVALID REQ");
		}
	}
	
	public double getTime() {
		double t = (System.currentTimeMillis() - startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
}
