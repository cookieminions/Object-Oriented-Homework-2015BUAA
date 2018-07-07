package elevator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

public class multi_scheduler extends als_scheduler implements Runnable{
	private request_list req_list;
	private elevator[] Ele;
	private Vector<Vector<request>> along_req;
	private Vector<Vector<request>> same_req;
	private double sys_t;
	private long start_t;
	
	public multi_scheduler(request_list req_l,long t) {
		req_list = req_l;
		start_t = t;
		Ele = new elevator[3];
		along_req = new Vector<Vector<request>>();
		for(int i=0;i<3;i++){
			along_req.add(new Vector<request>());
		}
		same_req = new Vector<Vector<request>>();
		for(int i=0;i<3;i++){
			same_req.add(new Vector<request>());
		}
	}
	
	public void run() {
		for(int i=0;i<3;i++){
			Ele[i] = new elevator(i);
			Ele[i].start();
		}
		while(true){
			schedule();
			Thread.yield();
		}
	}
	
	public void schedule() {
		for(int i=0;i<req_list.left_req();i++){
			request req = req_list.get_req(i);
			int ele_num = -1;
			int ele_numk = -1;
			
			if(req.get_type().equals("ER")){
				int ele_n = req.get_ele_num()-1;
				if(Ele[ele_n].get_main_req()==null&&along_req.elementAt(ele_n).size()!=0){
					Ele[ele_n].set_main_req(along_req.elementAt(ele_n).elementAt(0));
					//System.out.println("Er 1");
				}else if(Ele[ele_n].get_main_req()==null){
					//System.out.println("Er 2 "+req.get_des_floor());
					Ele[ele_n].set_main_req(req);//set main_req
					along_req.elementAt(ele_n).add(req);
					req_list.remove_req(i--);
				}
				else{//set same_req or along_req
					if((Ele[ele_n].get_direction().equals("UP")&&req.get_des_floor()>Ele[ele_n].get_floor())||
							(Ele[ele_n].get_direction().equals("DOWN")&&req.get_des_floor()<Ele[ele_n].get_floor())){
						//System.out.println("Er 0");
						int same_flag = 0;
						for(int j=0;j<along_req.elementAt(ele_n).size();j++){
							request tmp_req = along_req.elementAt(ele_n).elementAt(j);
							if(req.equal(tmp_req)){
								//System.out.println("Er 3");
								same_flag = 1;
								same_req.elementAt(ele_n).add(req);
								req_list.remove_req(i--);
								break;
							}
						}
						if(same_flag==0){
							//System.out.println("Er 4 "+Ele[ele_n].get_floor()+" "+Ele[i].get_aim_floor());
							along_req.elementAt(ele_n).add(req);
							req_list.remove_req(i--);
						}
					}
					else if(req.get_des_floor()==Ele[ele_n].get_floor()&&Ele[ele_n].get_status().equals("STILL")){
						//System.out.println("Er 5");
						same_req.elementAt(ele_n).add(req);
						req_list.remove_req(i--);
					}
				}
			}
			else{
			int same_flag = 0;
			for(int j=0;j<3;j++){
				for(int k=0;k<along_req.elementAt(j).size();k++){
					request tmp_req = along_req.elementAt(j).elementAt(k);
					if(req.equal(tmp_req)){
						same_req.elementAt(j).add(req);
						req_list.remove_req(i--);
						same_flag = 1;
						break;
					}
				}
				if(same_flag==1)	break;
			}
			if(same_flag!=1){
			for(int j=0;j<3;j++){
				if(Ele[j].get_main_req()==null&&along_req.elementAt(j).size()!=0){
					Ele[j].set_main_req(along_req.elementAt(j).elementAt(0));
				}
				if(Ele[j].get_main_req()!=null){
					if(Ele[j].get_status().equals("STILL")&&req.get_des_floor()==Ele[j].get_floor()&&req.get_direction().equals(Ele[j].get_direction())){
						same_req.elementAt(j).add(req);
						req_list.remove_req(i--);
						ele_num = -2;
						break;
					}
					else if((Ele[j].get_direction().equals(req.get_direction()))&&(
							(req.get_direction().equals("UP")&&req.get_des_floor()>Ele[j].get_floor()&&req.get_des_floor()<=Ele[j].get_aim_floor())||
							(req.get_direction().equals("DOWN")&&req.get_des_floor()<Ele[j].get_floor()&&req.get_des_floor()>=Ele[j].get_aim_floor()))){
						if(ele_num==-1)	ele_num = j;
						else{
							if(Ele[ele_num].get_move()>Ele[j].get_move())	ele_num = j;
						}
					}
				}else{
					if(ele_numk==-1)	ele_numk=j;
					else if(Ele[ele_numk].get_move()>Ele[j].get_move())	ele_numk = j;
				}
			}
			if(ele_num>=0){
				along_req.elementAt(ele_num).add(req);
				req_list.remove_req(i--);
			}
			else if(ele_num==-1&&ele_numk!=-1){
				along_req.elementAt(ele_numk).add(req);
				Ele[ele_numk].set_main_req(req);
				req_list.remove_req(i--);
			}
			}}
		}
		for(int i=0;i<3;i++){
			for(int j=0;j<along_req.get(i).size();j++){
				if(along_req.elementAt(i).elementAt(j).get_des_floor()==(Ele[i].get_floor()+1)&&
						!Ele[i].get_status().equals("STILL")&&Ele[i].get_flag()!=1){
					Ele[i].set_flag(1);
					//System.out.println(Ele[i].get_floor()+" "+Ele[i].get_status());
				}
			}
		}
		for(int i=0;i<3;i++){
			if(Ele[i].get_main_req()==null&&along_req.elementAt(i).size()!=0){
				Ele[i].set_main_req(along_req.elementAt(i).elementAt(0));
			}
			for(int j=0;j<along_req.get(i).size();j++){
				if(along_req.elementAt(i).elementAt(j).get_des_floor()==Ele[i].get_floor()&&
						Ele[i].get_status().equals("STILL")&&Ele[i].get_flag()==1){
					Print_Same(i,Ele[i].get_floor());
					Print_Req(i,Ele[i].get_floor(),get_time());
				}
			}
		}
		
	}
	
	public void Print_Same(int ele_num,int floor){
		String path = "result.txt";
		File file = new File(path);
		try{
			FileWriter fw = new FileWriter(file,true);
			for(int i=0;i<same_req.elementAt(ele_num).size();i++){
				request req = same_req.elementAt(ele_num).elementAt(i);
				if(req.get_des_floor()==floor){
					//System.out.println(same_req.get(ele_num).size()+" "+ele_num);
					fw.write(System.currentTimeMillis()+":SAME ["+req.toString()+","+req.get_req_t()+"]\r\n");
					same_req.get(ele_num).remove(i--);
				}
			}
			fw.close();
		}
		catch (IOException ex) {}
	}
	public void Print_Req(int ele_num,int floor,double t){
		String path = "result.txt";
		File file = new File(path);
		try{
			FileWriter fw = new FileWriter(file,true);
			int flag = 0;
			for(int i=0;i<along_req.elementAt(ele_num).size();i++){
				request req = along_req.elementAt(ele_num).elementAt(i);
				if(req.get_des_floor()==floor){
					//System.out.println("write");
					if(flag==0){
						fw.write(System.currentTimeMillis()+":["+req.toString()+","+req.get_req_t()+"]/(#"+
								(ele_num+1)+","+floor+","+Ele[ele_num].get_direction()+","+Ele[ele_num].get_move()+","+t+")\r\n");
						flag = 1;
					}
					else{
						fw.write(System.currentTimeMillis()+":["+req.toString()+","+req.get_req_t()+"]/(#"+
								(ele_num+1)+","+floor+",STILL,"+Ele[ele_num].get_move()+","+t+")\r\n");
					}
					along_req.get(ele_num).remove(i--);
				}
			}
			fw.close();
		}
		catch (IOException ex) {}
	}
	public double get_time(){
		sys_t = (System.currentTimeMillis()-start_t)/1000;
		sys_t = Double.parseDouble(new DecimalFormat("#0.0").format(sys_t));
		return sys_t;
	}
}
