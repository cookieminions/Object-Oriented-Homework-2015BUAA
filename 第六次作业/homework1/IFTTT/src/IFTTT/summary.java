package IFTTT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class summary implements Runnable{
	private String[] trigger_type = {"renamed","Modified","path-changed","size-changed"};
	private int[] trigger_record = {0,0,0,0};
	summary() {
	}
	
	public void run(){
		String path = "summary.txt";
		creatFile(path);
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			record_file(path);
		}
	}
	
	public synchronized void record(String trigger) {
		for(int i=0;i<4;i++){
			if(trigger.equals(trigger_type[i])){
				trigger_record[i]++;
				System.out.println("summary "+trigger_type[i]+" "+trigger_record[i]);
				break;
			}
		}
	}
	public void creatFile(String path) {
		File file = new File(path);
		if(file.exists()){
			try {
				FileWriter fw = new FileWriter(file);
				fw.write("");
				fw.close();
			} catch (IOException e){}
		}else{
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}
	}
	
	public synchronized void record_file(String path) {
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file);
			String record_summary = "";
			for(int i=0;i<4;i++){
				record_summary = record_summary + trigger_type[i]+" "+trigger_record[i]+"  ";
			}
			fw.write(record_summary);
			fw.close();
		} catch (IOException e) {}
	}
}
