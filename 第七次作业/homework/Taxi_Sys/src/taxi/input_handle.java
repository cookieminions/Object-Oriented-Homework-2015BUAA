package taxi;

import java.text.DecimalFormat;
import java.util.Scanner;

public class input_handle extends Thread{
	long startTime;
	private boolean running;
	private requestList reqList;
	private static Scanner s_in;
	private ReqCheck reqCheck;
	
	public input_handle(long t,requestList reqlist) {
		startTime = t;
		running = true;
		reqList = reqlist;
		s_in = new Scanner(System.in);
		reqCheck = new ReqCheck();
	}
	
	public void run(){
		while(running){
			request req = input();
			if(req!=null){
				reqList.addReq(req);
			}else{
				System.out.println("INVALID REQ");
			}
			yield();
		}
	}
	
	
	public request input(){
		String str = s_in.nextLine().replaceAll(" ","");
		double t = getTime();
		request req = reqCheck.checkReq(str,t);
		
		return req;
	}
	
	public double getTime() {
		double t = (System.currentTimeMillis() - startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
}
