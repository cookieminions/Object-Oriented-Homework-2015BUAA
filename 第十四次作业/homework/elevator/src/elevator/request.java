package elevator;

import java.util.regex.*;

public class request {
	private String[] req;
	private double req_t;
	private int des_floor;
	private double finish_time;
	private String req_str;
	
	
	private boolean judge;
	
	request(String str){
		judge = true;
		finish_time = 0.0;
		req_str = str;
		
		try{
			req = str.split("[(),]");
			req_t = Double.parseDouble(req[req.length-1]);
			des_floor = Integer.parseInt(req[2]);
		}
		catch(Exception e){
			req_t = 0.0;
			des_floor = 0;
			judge = false;
		}
		check_str(str);
	}
	
	public void check_str(String str){
		String regEx1 = "\\(FR\\,\\+?\\d+\\,UP\\,\\+?\\d+\\)";
		String regEx2 = "\\(FR\\,\\+?\\d+\\,DOWN\\,\\+?\\d+\\)";
		String regEx3 = "\\(ER\\,\\+?\\d+\\,\\+?\\d+\\)";
		Pattern pattern1 = Pattern.compile(regEx1);
		Pattern pattern2 = Pattern.compile(regEx2);
		Pattern pattern3 = Pattern.compile(regEx3);
		Matcher matcher1 = pattern1.matcher(str);
		Matcher matcher2 = pattern2.matcher(str);
		Matcher matcher3 = pattern3.matcher(str);
		if(judge){
			if(!req[0].equals(""))
				judge = false;
			if(req_t<0||req_t>2147483647.0){
				judge = false;
				return;
			}
			if(matcher1.matches()||matcher2.matches()){
				if((Integer.parseInt(req[2])==1&&req[3].equals("DOWN"))||
						(Integer.parseInt(req[2])==10&&req[3].equals("UP"))){
					judge = false;
					return;
				}
				if(Integer.parseInt(req[2])<=0||Integer.parseInt(req[2])>10){
					judge = false;
					return;
				}
			}
			else if(matcher3.matches()){
				if(Integer.parseInt(req[2])<=0||Integer.parseInt(req[2])>10){
					judge = false;
					return;
				}
			}
			else{
				judge = false;
				return;
			}	
		}
	}
	
	public boolean judge(){		
		return judge;
	}
	public String get_req_str(){
		return "["+req_str+"]";
	}
	public String get_s(){
		return req_str;
	}
	
	public String[] get_request(){
		return req;
	}
	public double get_req_t(){
		return req_t;
	}
	public int get_des_floor(){
		return des_floor;
	}
	public String get_req_class(){
		return req[1];
	}
	public String get_req_direction(){
		if(req[1].equals("FR"))
			return req[3];
		else
			return "ER";
	}
	
	public void set_finish_time(double t){
		finish_time = t;
	}
	public double get_finish_time(){
		return finish_time;
	}
	
	public String toString(){
		if(req[1].equals("FR")){
			String str = "["+req[1]+","+des_floor+","+req[3]+","+(int)req_t+"]";
			return str;
		}
		else{
			String str = "["+req[1]+","+des_floor+","+(int)req_t+"]";
			return str;
		}
	}
}
