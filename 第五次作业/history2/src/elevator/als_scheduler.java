package elevator;

import java.util.Vector;

public class als_scheduler extends scheduler{
	private request main_req;
	
	private Vector<request> along_req;
	
	als_scheduler(){
		super();
		along_req = new Vector<request>();
	}
	
	public void schedule(request_list req_list,elevator ele){
		int main_floor = main_req.get_des_floor();
		String e_direction = "STILL";
		int floor_flag = 0;
		t = t>main_req.get_req_t()?t:main_req.get_req_t();
		//finish_t = (main_floor-ele.get_floor())*0.5+t;
		
		if(ele.get_floor()<main_floor){ e_direction = "UP"; /*ele.set_floor(ele.get_floor()+1); t+=0.5;*/}
		else if(ele.get_floor()>main_floor){ e_direction = "DOWN"; /*ele.set_floor(ele.get_floor()-1); t+=0.5;*/}
		
		while(ele.get_floor()!=main_floor){
			int i = 0;
			int flag = 0;
			double e_t = floor_flag==0?(t-1):t;//解决主请求之后的第一条请求在之前主请求那一层可能出现错误捎带请求判断的问题
			floor_flag = 1;
			
			while(i<req_list.left_req() && req_list.get_req(i).get_req_t()<e_t){
				request tmp_req = req_list.get_req(i);
				if(tmp_req.get_des_floor()==ele.get_floor()){
					if(tmp_req.get_direction().equals(e_direction)||tmp_req.get_direction().equals("ER")){
						flag = 1;
						if(along_req.size()==0){
							along_req.add(tmp_req);
							System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
						}
						else if(along_req.size()==1){
							if(!tmp_req.get_type().equals(along_req.elementAt(0).get_type())){
								along_req.add(tmp_req);
								System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
							}
							else	System.out.println("SAME "+tmp_req.toString());
						}
						else	System.out.println("SAME "+tmp_req.toString());
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
					if(along_req.size()==1){
						if(tmp_req.get_direction().equals(along_req.elementAt(0).get_direction())){
							System.out.println("SAME "+tmp_req.toString());
							req_list.remove_req(i);
							i--;
						}
					}
					else if(along_req.size()==2){
						if(tmp_req.get_direction().equals(along_req.elementAt(0).get_direction())||
								tmp_req.get_direction().equals(along_req.elementAt(1).get_direction())){
							System.out.println("SAME "+tmp_req.toString());
							req_list.remove_req(i);
							i--;
						}
					}
				}
				i++;
			}
			if(flag == 1){t += 1;/*finish_t+=1*/}
			
			
			t += 0.5;
			
			if(along_req.size()!=0)	along_req.removeAllElements();
			//System.out.println(ele.get_floor()+ele.get_direction()+t);
		}
		if(ele.get_floor()==main_floor){
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
					if(tmp_req.get_direction().equals(e_direction)||tmp_req.get_direction().equals("ER")){//此处有误
						if(along_req.size()==1){
							if(!tmp_req.get_direction().equals(along_req.elementAt(0).get_direction())){
								along_req.add(tmp_req);
								if(e_direction=="STILL")	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+(t+1)+")");
								else	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
							}
							else	System.out.println("SAME "+tmp_req.toString());
						}
						else if(along_req.size()==2){
							if(!tmp_req.get_direction().equals(along_req.elementAt(0).get_direction())&&
									!tmp_req.get_direction().equals(along_req.elementAt(1).get_direction())){
								along_req.add(tmp_req);
								if(e_direction=="STILL")	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+(t+1)+")");
								else	System.out.println(tmp_req.toString()+"/("+ele.get_floor()+","+e_direction+","+t+")");
							}
							else	System.out.println("SAME "+tmp_req.toString());
						}
						else	System.out.println("SAME "+tmp_req.toString());
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
						if(tmp_req.get_direction().equals(along_req.elementAt(x).get_direction())){
							System.out.println("SAME "+tmp_req.toString());
							req_list.remove_req(i);
							i--;
							break;
						}
					}
				}
				i++;
			}
			if(along_req.size()!=0)	along_req.removeAllElements();
			t += 1;
			int flag = 0;
			for(i=0;i<req_list.left_req();i++){
				if(req_list.get_req(i).get_req_t()<t-1&&req_list.get_req(i).get_type().equals("ER")){
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
		System.out.println("main_req floor"+main_req.get_des_floor());
	}
	
	public void command(request_list req_list,elevator ele){
		if(req_list.left_req()!=0)
			main_req = req_list.get_req(0);
		while(req_list.left_req()!=0){
			schedule(req_list,ele);
		}
	}
}
