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
				if(req.get_req_t()==0)
					req_list.add(req);
				else
					System.out.println("请求无效");
			}
			else if(req.get_req_t()>=req_list.lastElement().get_req_t()){
				req_list.add(req);
			}
			else
				System.out.println("请求无效");
		}
		else
			System.out.println("请求无效");
	}
	
	public void remove_req(int i){
		req_list.remove(i);
	}
	
	public int left_req(){
		return req_list.size();
	}
	
	public request get_req(int i){
		return (req_list.elementAt(i));
	}
	
}
