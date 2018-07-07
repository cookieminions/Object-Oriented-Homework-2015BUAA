package IFTTT;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileList {
	private Map<String, safe_file> filelist;
	
	FileList() {
		filelist = new HashMap<String,safe_file>();
	}
	
	public synchronized safe_file get_shell(String path){
		safe_file file = getFile(path);
		if(file==null){
			file = new safe_file(path);
			addFile(path, file);
		}else{
			if(!file.exist()){
				remove(path);
				file = new safe_file(path);
				addFile(path, file);
			}
		}
		return file;
	}
	
	public synchronized void write(safe_file file,String content,boolean f){
		file.writefile(content,f);
	}
	
	public synchronized safe_file rename(safe_file file,String newname){
		if(file.exist()&&file.isFile()){
			String parent = file.getParent();
			String path = parent + "\\" + newname;
			safe_file newfile = get_shell(path);
			if(newfile.exist()){
				System.out.println(newname+" has been there");
			}else{
				boolean flag = file.private_renameTo(newfile);
				if(!flag)	System.out.println("rename fail");
				else{
					file = newfile;
					return newfile;
				}
			}
		}else{
			System.out.println("no file or file is Directory");
		}
		return file;
	}
	
	public synchronized safe_file moveTo(safe_file file,String directory){
		if(file.exist()&&file.isFile()){
			String name = file.getName();
			String path = directory+"\\"+name;
			safe_file newfile = get_shell(path);
			if(newfile.exist()){
				System.out.println(name+" has been "+directory);
			}else{
				try {
					boolean flag = file.private_renameTo(newfile);
					if(flag){
						file = newfile;
						return newfile;
					}else	System.out.println("move fail");
				} catch (Exception e) {
					System.out.println("move fail");
				}
			}
		}else{
			System.out.println("no file or file is Directory");
		}
		return file;
	}
	
	
	private synchronized safe_file getFile(String path){
		return filelist.get(path);
	}
	
	private synchronized void addFile(String path,safe_file file){
		filelist.put(path, file);
	}
	
	private synchronized void remove(String path){
		filelist.remove(path);
	}
	
	public synchronized void refresh() {
		Iterator<Map.Entry<String, safe_file>> it = filelist.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, safe_file> entry=it.next();
			//String key = entry.getKey();
			safe_file value = entry.getValue();
			if(!value.exist())	{
		    	it.remove();
		    }
		}
	}
}
