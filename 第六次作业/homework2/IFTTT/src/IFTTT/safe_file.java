package IFTTT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class safe_file {
	private File file;
	
	safe_file(String path){
		file = new File(path);
	}
	
	public synchronized void writefile(String content,boolean f){
		if(file.exists()&&file.isFile()){
			try {
				FileWriter fw = new FileWriter(file,f);
				fw.write(content);
				fw.close();
			} catch (IOException e) {
				System.out.println("cannot write");
			}
		}else{
			System.out.println("no file");
		}
	}
	public synchronized void delete(){
		if(file.exists()){
			try{
				boolean flag = file.delete();
				if(!flag){
					System.out.println("delete fail");
				}
			}catch(Exception e){
				System.out.println("delete fail");
			}
		}else{
			System.out.println("no file");
		}
	}
	public synchronized void createNew(String type){
		if(file.exists()){
			System.out.println("file exits");
		}else{
			if(type.equals("File")){
				try {
					file.createNewFile();
				} catch (IOException e) {
					System.out.println("create fail");
				}
			}
			else if(type.equals("Directory")){
				boolean flag = file.mkdir();
				if(!flag)	System.out.println("create fail");
			}
		}
	}
	
	public synchronized File getfile() {
		return file;
	}
	public synchronized boolean private_renameTo(safe_file newfile){
		return (file.renameTo(newfile.getfile()));
	}
	
	public synchronized boolean isDirectory() {
		return file.isDirectory();
	}
	public synchronized boolean isFile() {
		return file.isFile();
	}
	
	public synchronized String getName(){
		return file.getName();
	}
	public synchronized String getPath(){
		return file.getPath();
	}
	public synchronized String getParent(){
		return file.getParent();
	}
	public synchronized long lastModified(){
		return file.lastModified();
	}
	public synchronized long getlength(){
		if(file.isFile())
			return file.length();
		else return 0;
	}
	public synchronized boolean exist(){
		return file.exists();
	}
	public synchronized String[] filelist(){
		String[] strlist = file.list();
		if(strlist!=null){
			for(int i=0;i<strlist.length;i++){
				strlist[i] = getPath()+"\\"+strlist[i];
				//System.out.println(strlist[i]);
			}
		}
		return strlist;
	}
}
