package elevator;

import java.util.Vector;

public class request_list{
	private Vector<request> req_list; 
	
	request_list(){
		req_list = new Vector<request>();
	}
	
	public void add_req(request req){
		if(req.judge()){
			if(req_list.size()==0){
				if(req.get_req_t()==0){
					if(req.toString().equals("[FR,1,UP,0]")||
							(req.get_req_class().equals("FR")&&req.get_des_floor()==1&&req.get_req_direction().equals("UP")))
						req_list.add(req);
					else	
						System.out.println("INVALID "+req.toString());
				}
				else
					System.out.println("INVALID "+req.toString());
			}
			else if(req.get_req_t()>=req_list.lastElement().get_req_t()){
				req_list.add(req);
			}
			else
				System.out.println("INVALID "+req.toString());
		}
		else
			System.out.println("INVALID "+req.get_req_str());
	}
	
	public void remove_req(int i){
		req_list.remove(i);
	}
	public void remove_req(request req){
		req_list.remove(req);
	}
	
	public int left_req(){
		return req_list.size();
	}
	
	public request get_req(int i){
		return (req_list.elementAt(i));
	}
	
}
