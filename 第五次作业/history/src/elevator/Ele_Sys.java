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
		Input_Handle i_h = new Input_Handle(req_list, System.currentTimeMillis());
		multi_scheduler m_s = new multi_scheduler(req_list);
		i_h.start();
		m_s.start();
	}
	
	
}
