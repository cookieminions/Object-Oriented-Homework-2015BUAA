package elevator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ele_Sys {
	public static void creatFile(){
		String path = "result.txt";
		File file = new File(path);
		
		if(file.exists()){
			try{
				FileWriter fw = new FileWriter(file);
				fw.write("");
				fw.close();
			}
			catch(IOException ex) {
			}
		}
		else{
			try {
				file.createNewFile();				
			} catch (IOException e) {
			}
		}
	}
	
	public static void main(String[] args) {
		creatFile();
		request_list req_list = new request_list();
		long start_t = System.currentTimeMillis();
		Input_Handle i_h = new Input_Handle(req_list, start_t);
		multi_scheduler m_s = new multi_scheduler(req_list,start_t);
		Thread m_s_t = new Thread(m_s);
		i_h.start();
		m_s_t.start();
	}
	
	
}
