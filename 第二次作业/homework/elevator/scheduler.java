package elevator;

import java.util.Scanner;

public class scheduler {
	private double t;
	private int iterator;
	private static Scanner s_in;
	
	scheduler(){
		t = 0.0;
		iterator = 0;
	}
	
	public void schedule(elevator ele,request req,request_list req_list){
		int next_floor = 0;
		double req_t = (double)req.get_req_t();
		
		next_floor = Integer.parseInt(req.get_request()[2]);
		
		if(req.get_request()[1].equals("FR")||req.get_request()[1].equals("ER")){
			int i = iterator-1;
			int flag = 1;
			while(i>=0){
				request pre_req = req_list.get_req(i);
				//System.out.println(pre_req.get_finish_time());
				if(pre_req.get_request()[1].equals(req.get_request()[1])&&pre_req.get_request()[2].equals(req.get_request()[2])
						&&(req.get_request()[1].equals("ER")||pre_req.get_request()[3].equals(req.get_request()[3]))){
					if(req_t<=pre_req.get_finish_time()){
						req_list.remove_req(iterator);
						flag = 0;
						String req1 = Make_String(req.get_request());
						String req2 = Make_String(pre_req.get_request());
						System.out.println("请求"+req1+"与请求"+req2+"为相同请求");
						break;
					}
				}
				i--;
			}
			if(flag==1){
				if(t<req_t)
					t = req_t;
				t += 0.5*Math.abs(next_floor-ele.get_floor())+1.0;
				ele.set_floor(next_floor);
				req.set_finish_time(t);
				if(ele.get_direction().equals("STILL"))
					System.out.println("("+ele.get_floor()+","+ele.get_direction()+","+(t)+")");
				else
					System.out.println("("+ele.get_floor()+","+ele.get_direction()+","+(t-1)+")");
				iterator++;
			}
		}	
	}
	
	public void command(elevator ele,request_list req_list){
		while(iterator<req_list.left_req()){
			request req = req_list.get_req(iterator);
			schedule(ele,req,req_list);
		}
	}
	
	public String Make_String(String[] req){
		if(req[1].equals("FR")){
			String str = "("+req[1]+","+req[2]+","+req[3]+","+req[4]+")";
			return str;
		}
		else{
			String str = "("+req[1]+","+req[2]+","+req[3]+")";
			return str;
		}
	}
	
	public static void main(String[] args){
		s_in = new Scanner(System.in);
		
		elevator ele = new elevator();
		request_list req_list = new request_list();
		scheduler manager = new scheduler();
		
		String str = "";
		try{
			while(!(str = s_in.nextLine().replaceAll(" ","")).equals("run")){
				request req = new request(str);
				req_list.add_req(req);
			}
			s_in.close();
			manager.command(ele, req_list);
		}
		catch(Exception e){
			System.out.println("请求无效");
		}
		
	}
	
}
