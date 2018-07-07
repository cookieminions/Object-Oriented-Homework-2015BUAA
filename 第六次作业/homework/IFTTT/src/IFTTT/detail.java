package IFTTT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class detail implements Runnable{
	private Vector<Vector<String>> detail_record;
	
	detail(){
		detail_record = new Vector<Vector<String>>();
		for(int i=0;i<4;i++){
			detail_record.add(new Vector<String>());
		}
	}
	
	public void run(){
		String path = "detail.txt";
		creatFile(path);
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
			record_file(path);
		}
	}
	
	public synchronized void record(String trigger,file_target last,file_target curr,file_target main_target) {
		if(trigger.equals("renamed")){
			String record_detail = "monitor: "+main_target.path+"; "+"renamed "+last.path+" lastname: "+last.name+", "+curr.path+" currname: "+curr.name;
			detail_record.get(0).add(record_detail);
			System.out.println("detail "+record_detail);
		}
		else if(trigger.equals("Modified")){
			String record_detail = "monitor: "+main_target.path+"; "+"Modified "+last.path+" lastModified: "+last.modified_t+", "+curr.path+" currModified: "+curr.modified_t;
			detail_record.get(1).add(record_detail);
			System.out.println("detail "+record_detail);
		}
		else if(trigger.equals("path-changed")){
			String record_detail = "monitor: "+main_target.path+"; "+"path-changed "+"lastpath: "+last.path+", currpath: "+curr.path;
			detail_record.get(2).add(record_detail);
			System.out.println("detail "+record_detail);
		}
		else if(trigger.equals("size-changed")){
			long lastsize = 0;
			long currsize = 0;
			String lastpath = "null";
			String currpath = "null";
			if(last!=null){
				lastsize = last.size;
				lastpath = last.path;
			}
			if(curr!=null){
				currsize = curr.size;
				currpath = curr.path;
			}
			String record_detail = "monitor: "+main_target.path+"; "+"size-changed "+lastpath+" lastsize: "+lastsize+", "+currpath+" currsize: "+currsize;
			detail_record.get(3).add(record_detail);
			System.out.println("detail "+record_detail);
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
			FileWriter fw = new FileWriter(file,true);
			for(int i=0;i<4;i++){
				for(int j=0;j<detail_record.get(i).size();j++){
					String str = detail_record.get(i).get(j)+"\r\n";
					detail_record.get(i).remove(j--);
					fw.write(str);
				}
			}
			fw.close();
		} catch (IOException e) {}
	}

}
