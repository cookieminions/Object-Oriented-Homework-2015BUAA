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
		/* 
		  @MODIFIES: startTime, running, reqList, s_in, reqCheck;
		  @EFFECTS: 
		  */
		startTime = t;
		running = true;
		reqList = reqlist;
		s_in = new Scanner(System.in);
		reqCheck = new ReqCheck();
	}
	
	public void run(){
		/* 
		  @MODIFIES: s_in, reqList;
		  @EFFECTS: (input()!=null) ==> reqList.addReq(input());
		  */
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
		/*
		  @MODIFIES: s_in;
		  @EFFECTS: \result == reqCheck.checkReq(str,t); 
		  */
		String str = s_in.nextLine().replaceAll(" ","");
		double t = getTime();
		request req = reqCheck.checkReq(str,t);
		
		return req;
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
