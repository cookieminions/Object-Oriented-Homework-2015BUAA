package elevator;

import java.util.Scanner;

public class EleMain {
	public static void main(String[] args){
		Scanner s_in = new Scanner(System.in);
		
		request_list req_list = new request_list();
		als_scheduler manager = new als_scheduler();
		
		String str = "";
		try{
			while(!(str = s_in.nextLine().replaceAll(" ","")).equals("run")){
				request req = new request(str);
				req_list.add_req(req);
			}
			s_in.close();
			
			manager.command(req_list);
		}
		catch(Exception e){
			System.out.println("INVALID [Ctrl-Z]");
		}
	}
}
