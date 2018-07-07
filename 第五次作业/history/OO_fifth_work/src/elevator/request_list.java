package elevator;

import java.util.Vector;

public class request_list {
	private Vector<request> req_list;
	
	request_list(){
		req_list = new Vector<request>();
	}
	
	public synchronized void add_req(request req) {
		req_list.add(req);
	}
	public synchronized void remove_req(int i){
		req_list.remove(i);
	}
	public synchronized void remove_req(request req){
		req_list.remove(req);
	}
	public synchronized int left_req(){
		return req_list.size();
	}
	public synchronized request get_req(int i){
		return (req_list.elementAt(i));
	}
	
}
