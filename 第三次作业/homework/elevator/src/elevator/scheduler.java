package elevator;

public class scheduler {
	protected double t;
	protected int iterator;
	protected elevator ele;
	
	scheduler(){
		t = 0.0;
		iterator = 0;
		ele = new elevator();
	}
	
	public void schedule(request_list req_list){
		request req = req_list.get_req(iterator);
		int next_floor = 0;
		double req_t = (double)req.get_req_t();
		
		next_floor = Integer.parseInt(req.get_request()[2]);
		
		if(req.get_request()[1].equals("FR")||req.get_request()[1].equals("ER")){
			int i = iterator-1;
			int flag = 1;
			while(i>=0){
				request pre_req = req_list.get_req(i);
				//System.out.println(pre_req.get_finish_time());
				if(pre_req.get_request()[1].equals(req.get_request()[1])&&pre_req.get_des_floor()==req.get_des_floor()
						&&(req.get_request()[1].equals("ER")||pre_req.get_request()[3].equals(req.get_request()[3]))){
					if(req_t<=pre_req.get_finish_time()){
						req_list.remove_req(iterator);
						flag = 0;
						System.out.println("请求"+req.toString()+"与请求"+pre_req.toString()+"为相同请求");
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
	
	public void command(request_list req_list){
		while(iterator<req_list.left_req()){
			schedule(req_list);
		}
	}
	
	
}
