package elevator;

import java.util.regex.Pattern;

public class request {
	private String[] req;
	private double req_t;
	
	private boolean judge;
	private double finish_time;
	
	request(String str){
		judge = true;
		finish_time = 0.0;
		try{
			req = str.split("[(),]");
			//if(req[req.length-1].length()>10)	judge = false;
			req_t = Double.parseDouble(req[req.length-1]);
		}
		catch(Exception e){
			req_t = 0.0;
			judge = false;
		}
	}
	
	public boolean judge(){
		if(judge){
			Pattern pattern = Pattern.compile("[0-9]+");
			if((req.length==5)&&(req[0].equals(""))&&(req[1].equals("FR"))
					&&(pattern.matcher(req[2]).matches()&&Integer.parseInt(req[2])>0&&Integer.parseInt(req[2])<=10)
					&&(req[3].equals("DOWN")||req[3].equals("UP"))
					&&(pattern.matcher(req[4]).matches()&&Double.parseDouble(req[4])>=0.0&&Double.parseDouble(req[4])<=9999999999.0)){
				return true;
			}
			else if((req.length==4)&&(req[0].equals(""))&&(req[1].equals("ER"))
					&&(pattern.matcher(req[2]).matches()&&Integer.parseInt(req[2])>0&&Integer.parseInt(req[2])<=10)
					&&(pattern.matcher(req[3]).matches()&&Double.parseDouble(req[3])>=0.0&&Double.parseDouble(req[3])<=9999999999.0)){
				return true;
			}
			else
				return false;
		}
		return judge;
	}
	
	public String[] get_request(){
		return req;
	}
	public double get_req_t(){
		return req_t;
	}
	
	public void set_finish_time(double t){
		finish_time = t;
	}
	public double get_finish_time(){
		return finish_time;
	}
	
}
