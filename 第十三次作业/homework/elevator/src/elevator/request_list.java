package elevator;

import java.util.Vector;

/* Overview: request_list类保存了请求队列 能够往队列中加入和取出请求 也能移除请求 能够返回请求队列的长度等相关信息
 * Objects: Vector<request> req_list
 * Abstraction Function: AF(c) = (req_list), where
 * 		req_list = c.req_list
 * Invariant: (req_list!=null)
 * */
public class request_list{
	Vector<request> req_list; 
	
	public boolean repOk() {
		/*
		  @MODIFIES: None
		  @EFFECTS: (this.req_list==null)==>\result == false
		  			(otherwise)==>\result==true
		  */
		if(req_list==null)	return false;
		return true;
	}
	
	request_list(){
		/*
		  @MODIFIES: this 
		  */
		req_list = new Vector<request>();
	}
	
	public void add_req(request req){
		/*@REQUIRES: req!=null;
		  @MODIFIES: this
		  @EFFECTS: req.judge()&&((req.req_t==0&&(format of req is [FR,1,UP,0]))||(req.req_t>=(last req in \old(req_list)).req_t))
		  				==>	(req_list.contains(req)==true)&&(req_list.size == \old(req_list).size+1)
		  */
		if(req.judge()){
			if(req_list.size()==0){
				if(req.get_req_t()==0){
					if((req.get_req_class().equals("FR")&&req.get_des_floor()==1&&req.get_req_direction().equals("UP")))
						req_list.add(req);
					else	System.out.println("INVALID "+req.toString());
				}
				else	System.out.println("INVALID "+req.toString());
			}
			else if(req.get_req_t()>=req_list.lastElement().get_req_t()){
				req_list.add(req);
			}
			else	System.out.println("INVALID "+req.toString());
		}
		else	System.out.println("INVALID "+req.get_req_str());
	}
	
	public void remove_req(int i){
		/*@REQUIRES: i>=0&&i<req_list.size
		  @MODIFIES: this
		  @EFFECTS: (req_list.contains(req_list[i])==false)&&(req_list.size == \old(req_list).size-1)
		  */
		req_list.remove(i);
	}
	public void remove_req(request req){
		/*@REQUIRES: req!=null && req_list.contains(req)==true;
		  @MODIFIES: this
		  @EFFECTS: (req_list.contains(req)==false)&&(req_list.size == \old(req_list).size-1)
		  */
		req_list.remove(req);
	}
	
	public int left_req(){
		/* 
		  @MODIFIES: None
		  @EFFECTS: \result == req_list.size
		  */
		return req_list.size();
	}
	
	public request get_req(int i){
		/*@REQUIRES: i>=0&&i<req_list.size
		  @MODIFIES: None
		  @EFFECTS: \result == req_list[i]
		  */
		return (req_list.elementAt(i));
	}
	
}
