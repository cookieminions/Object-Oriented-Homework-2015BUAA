package elevator;

/* Overview: scheduler 类负责将请求队列中的请求取出并分配给电梯 在电梯运行过程中进行傻瓜调度
 * Objects: double t, int iterator, elevator ele
 * Abstraction Function: AF(c) = (t, iterator, ele), where
 * 		t = c.t, iterator = c.iterator, ele = c.ele
 * Invariant: (ele!=null)
 * */
public class scheduler {
	protected double t;
	protected int iterator;
	protected elevator ele;
	
	scheduler(){
		/*
		  @MODIFIES: this
		  */
		t = 0.0;
		iterator = 0;
		ele = new elevator();
	}
	
	public boolean repOk(){
		/*
		  @MODIFIES: None
		  @EFFECTS: (this.ele==null)==>\result == false
		  			(otherwise)==>\result==true
		  */
		if(ele==null)	return false;
		return true;
	}
	
	public void schedule(request_list req_list){
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: (response every req and move to destionation) && (make ele move) && (t change with ele) && 
		  			(iterator plus if req is not a same req) && 
		  			(\all requset req; (req in req_list)&&(req is same with another req which is responsed); req_list.contains(req)==false && req be ignored)
		  */
		request req = req_list.get_req(iterator);
		int next_floor = 0;
		double req_t = (double)req.get_req_t();
		
		next_floor = Integer.parseInt(req.get_request()[2]);
		
		if(req.get_request()[1].equals("FR")||req.get_request()[1].equals("ER")){
			int i = iterator-1;
			int flag = 1;
			while(i>=0){
				request pre_req = req_list.get_req(i);
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
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: response all reqs in req_list && iterator == req_list.size &&
		  			(\all requset req; (req in req_list)&&(req is same with another req which is responsed); req_list.contains(req)==false && req be ignored)
		  */
		while(iterator<req_list.left_req()){
			schedule(req_list);
		}
	}
	
	
}
