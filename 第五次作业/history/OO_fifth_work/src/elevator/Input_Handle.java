package elevator;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.*;

public class Input_Handle extends Thread{
	private static Scanner s_in;
	private double req_t;
	private long start_t;
	
	private request_list req_list;
	
	Input_Handle(request_list req_l,long t) {
		req_list = req_l;
		start_t = t;
		s_in = new Scanner(System.in);
	}
	
	public void run() {
		while(true){
			Vector<request> req_l = input();
			for(int i=0;i<req_l.size();i++){
				req_list.add_req(req_l.elementAt(i));
			}
			yield();
		}
	}
	
	private Vector<String> format_check(String line){
		Vector<String> req_l = new Vector<String>();
		String[] tmp_req_l = line.split(";");
		
		String regEx1 = "\\(FR\\,\\+?\\d+\\,UP\\)";
		String regEx2 = "\\(FR\\,\\+?\\d+\\,DOWN\\)";
		String regEx3 = "\\(ER\\,#[1-3]\\,\\+?\\d+\\)";
		
		Pattern pattern1 = Pattern.compile(regEx1);
		Pattern pattern2 = Pattern.compile(regEx2);
		Pattern pattern3 = Pattern.compile(regEx3);
		
		for(int i=0;i<tmp_req_l.length;i++){//catch 10 requests
			String str = tmp_req_l[i];
			boolean judge = true;
			
			if(i>=10){
				//System.out.println(System.currentTimeMillis()+":INVALID ["+str+", "+req_t+"]");
				Print_Invalid(str);
				continue;
			}
			
			Matcher matcher1 = pattern1.matcher(str);
			Matcher matcher2 = pattern2.matcher(str);
			Matcher matcher3 = pattern3.matcher(str);
			
			if(matcher1.matches()||matcher2.matches())	judge = content_check(str, 0);
			else if(matcher3.matches())					judge = content_check(str, 1);
			else										judge = false;
			
			if(judge)	req_l.add(str);
			else{
				//System.out.println(System.currentTimeMillis()+":INVALID ["+str+", "+req_t+"]");
				Print_Invalid(str);
			}
		}
		return req_l;
	}
	
	private boolean content_check(String str,int type){
		boolean judge = true;
		int req_floor = 0;
		int des_floor = 0;
		
		String[] req;
		try{
			req = str.split("[(),]");
			if(type==0)		req_floor = Integer.parseInt(req[2]);
			else if(type==1)	des_floor = Integer.parseInt(req[3]);
		}
		catch (Exception e) {
			return false;
		}
		
		if(type==0){
			if(req_floor<1||req_floor>20)	judge = false;
			else if(req_floor==1){
				if(req[3].equals("DOWN"))	judge = false;
			}
			else if(req_floor==20){
				if(req[3].equals("UP"))		judge = false;
			}
		}
		else if(type==1&&(des_floor<1||des_floor>20))	judge = false;
		
		return judge;
	}
	
	public Vector<request> input(){
		Vector<request> req_l = new Vector<request>();
		try{
			String line = s_in.nextLine();
			get_time();
			Vector<String> req_l_str = format_check(line.replaceAll(" ", ""));
			for(int i=0;i<req_l_str.size();i++){
				request req = new request(req_l_str.elementAt(i), req_t);
				req_l.add(req);
			}
		}
		catch (Exception e) {}
		return req_l;
	}
	
	public void Print_Invalid(String str) {
		String path = "result.txt";
		
		File file = new File(path);
		try{
			FileWriter fw = new FileWriter(file,true);
			fw.write(System.currentTimeMillis()+":INVALID ["+str+", "+req_t+"]\r\n");
			fw.close();
		}
		catch (IOException ex) {}
	}
	
	public void get_time(){
		req_t = (System.currentTimeMillis()-start_t)/1000;
		req_t = Double.parseDouble(new DecimalFormat("#0.0").format(req_t));
	}

		
}
