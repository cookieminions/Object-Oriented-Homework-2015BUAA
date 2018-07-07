package elevator;

import java.util.Vector;

/* Overview: new_scheduler 类负责将请求队列中的请求取出并分配给电梯 在电梯运行过程中对可捎带的请求再进行分配 并忽略相同请求
 * Objects: double t, int iterator, elevator ele, int main_floor, request main_req, String e_direction, int floor_flag
 * Abstraction Function: AF(c) = (t, iterator, ele, main_floor, main_req, e_direction), where
 * 		t = c.t, iterator = c.iterator, ele = c.ele, main_floor = c.main_floor, main_req = c.main_req, e_direction = c.e_direction
 * Invariant: (ele!=null)&&(e_direction!=null)&&(along_req!=null)&&(\all object req;req in along_req;instanceof req == request)
 * */
public class als_scheduler extends scheduler{
	request main_req;
	int main_floor;
	String e_direction;
	Vector<request> along_req;
	
	public boolean repOk() {
		/*
		  @MODIFIES: None
		  @EFFECTS: (this.ele==null||this.e_direction==null||this.along_req==null)==>\result == false
		  			(otherwise)==>\result==true
		  */
		if(!super.repOk())	return false;
		if(e_direction==null||along_req==null)	return false;
		return true;
	}
	
	als_scheduler(){
		/*
		  @MODIFIES: this
		  */
		super();
		main_floor = 0;
		e_direction = "";
		along_req = new Vector<request>();
	}
	
	public void Move2Des(request_list req_list){
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: (\all requset req; (req in req_list)&&(req can be picked up when move to destination); req_list.contains(req)==false && req be response)&&
		  			(\all requset req; (req in req_list)&&(req is same with another req which is responsed); req_list.contains(req)==false && req be ignored)&&
		  			(this.ele.floor == main_floor)&&(t change with the main_floor)
		  */
		int floor_flag = 0;
		while(ele.get_floor()!=main_floor){
			int i = 0;
			int flag = 0;
			double e_t = floor_flag==0?(t-1):t;
			floor_flag = 1;
			while(i<req_list.left_req() && req_list.get_req(i).get_req_t()<e_t){
				request tmp_req = req_list.get_req(i);
				if(tmp_req.get_des_floor()==ele.get_floor()){
					if(tmp_req.get_req_direction().equals(e_direction)||tmp_req.get_req_direction().equals("ER")){
						flag = 1;
						if(along_req.size()==0){
							along_req.add(tmp_req);
							System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
						}else if(along_req.size()==1){
							if(!tmp_req.get_req_class().equals(along_req.elementAt(0).get_req_class())){
								along_req.add(tmp_req);
								System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
							}else	System.out.println("SAME "+tmp_req.toString());
						}else	System.out.println("SAME "+tmp_req.toString());
						req_list.remove_req(i);
						i--;
					}
				}
				i++;
			}
			while(i<req_list.left_req() && req_list.get_req(i).get_req_t()<=e_t+1){
				request tmp_req = req_list.get_req(i);
				if(flag!=1)	break;
				if(tmp_req.get_des_floor()==ele.get_floor()&&flag==1){
					int size = along_req.size();
					for(int x=0;x<size;x++){
						if(tmp_req.get_req_direction().equals(along_req.elementAt(x).get_req_direction())){
							System.out.println("SAME "+tmp_req.toString());
							req_list.remove_req(i);
							i--;
						}
					}
				}i++;
			}
			if(flag == 1)	t += 1;
			if(ele.get_floor()<main_floor)	ele.set_floor(ele.get_floor()+1);
			else if(ele.get_floor()>main_floor)	ele.set_floor(ele.get_floor()-1);
			t += 0.5;
			if(along_req.size()!=0)	along_req.removeAllElements();
		}
	}
	
	public void ArriveAtDes(request_list req_list) {
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: (\all requset req; (req in req_list)&&(req can be picked up when arrived at destination); req_list.contains(req)==false && req be response)&&
		  			(\all requset req; (req in req_list)&&(req is same with another req which is responsed); req_list.contains(req)==false && req be ignored)&&
		  			(req_list.contains(main_req)=false)&&(t = \old(t)+1)
		  */
		along_req.add(main_req);
		if(e_direction=="STILL")
			System.out.println(main_req.toString()+"/("+ele.get_floor()+","+e_direction+","+(t+1)+")");
		else
			System.out.println(main_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
		req_list.remove_req(main_req);
		
		int i=0;
		while(i<req_list.left_req() && req_list.get_req(i).get_req_t()<t){
			request tmp_req = req_list.get_req(i);
			if(tmp_req.get_des_floor()==ele.get_floor()){
				if(tmp_req.get_req_direction().equals(e_direction)||tmp_req.get_req_direction().equals("ER")||(tmp_req.get_req_class().equals("FR")&&tmp_req.get_req_direction().equals(main_req.get_req_direction()))){
					if(along_req.size()==1){
						if(!tmp_req.get_req_direction().equals(along_req.elementAt(0).get_req_direction())){
							along_req.add(tmp_req);
							if(e_direction=="STILL")	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+(t+1)+")");
							else	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
						}else	System.out.println("SAME "+tmp_req.toString());
					}else if(along_req.size()==2){
						if(!tmp_req.get_req_direction().equals(along_req.elementAt(0).get_req_direction())&&
								!tmp_req.get_req_direction().equals(along_req.elementAt(1).get_req_direction())){
							along_req.add(tmp_req);
							if(e_direction=="STILL")	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+(t+1)+")");
							else	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
						}else	System.out.println("SAME "+tmp_req.toString());
					}else	System.out.println("SAME "+tmp_req.toString());
					req_list.remove_req(i);
					i--;
				}
			}
			i++;
		}
		while(i<req_list.left_req() && req_list.get_req(i).get_req_t()<=t+1){
			request tmp_req = req_list.get_req(i);
			if(tmp_req.get_des_floor()==ele.get_floor()){
				int size = along_req.size();
				for(int x=0;x<size;x++){
					if(tmp_req.get_req_direction().equals(along_req.elementAt(x).get_req_direction())){
						System.out.println("SAME "+tmp_req.toString());
						req_list.remove_req(i);
						i--;
						break;
					}
				}
			}i++;
		}
		if(along_req.size()!=0)	along_req.removeAllElements();
		t += 1;
	}
	
	public void ResetMainReq(request_list req_list) {
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: set main_req after ele arrive at destination
		  */
		int flag = 0;
		for(int i=0;i<req_list.left_req();i++){
			if(req_list.get_req(i).get_req_t()<t-1&&req_list.get_req(i).get_req_class().equals("ER")){
				if(e_direction.equals("UP")&&req_list.get_req(i).get_des_floor()>main_req.get_des_floor()){
					main_req = req_list.get_req(i);
					flag = 1;
					break;
				}
				else if(e_direction.equals("DOWN")&&req_list.get_req(i).get_des_floor()<main_req.get_des_floor()){
					main_req = req_list.get_req(i);
					flag = 1;
					break;
				}
			}
		}
		if(flag==0&&req_list.left_req()!=0){ main_req = req_list.get_req(0);}
	}
	
	public void schedule(request_list req_list){
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: (response main_req and other reqs which can be picked up moving to destionation) && (make ele move)
		  */
		main_floor = main_req.get_des_floor();
		e_direction = "STILL";
		t = t>main_req.get_req_t()?t:main_req.get_req_t();
		
		if(ele.get_floor()<main_floor){ e_direction = "UP"; }
		else if(ele.get_floor()>main_floor){ e_direction = "DOWN"; }
		
		Move2Des(req_list);
		ArriveAtDes(req_list);
		ResetMainReq(req_list);
		
		//System.out.println("main_req floor"+main_req.get_des_floor());
	}
	
	public void command(request_list req_list){
		/*@REQUIRES: req_list!=null
		  @MODIFIES: this, req_list
		  @EFFECTS: response all reqs in req_list&&(req_list.size == 0)
		  */
		if(req_list.left_req()!=0)
			main_req = req_list.get_req(0);
		while(req_list.left_req()!=0){
			schedule(req_list);
		}
	}
}
