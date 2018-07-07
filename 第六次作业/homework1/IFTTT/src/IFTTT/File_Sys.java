package IFTTT;

import java.util.Vector;

public class File_Sys {
	
	public static file_target get_target(Task task,FileList filelist){
		String path = task.get_path();
		safe_file file = filelist.get_shell(path);
		//System.out.println(task.toString());
		if(file.exist()){
			String parent_d = file.getParent();
			String type = file.isFile()?"File":"Directory";
			String name = file.getName();
			file_target main_target = new file_target(type, name, parent_d, path, 0, 0);
			return main_target;
		}else{
			System.out.println("INVALID "+task.toString());
			return null;
		}
	}
	
	public static void main(String args[]){
		FileList file_shell = new FileList();
		summary mySummary = new summary();
		detail myDetail = new detail();
		
		try{
			Handle handle = new Handle();
			handle.input_handle();
			Vector<Task> task_list = handle.get_tasklist();
			
			for(int i=0;i<task_list.size();i++){
				file_target main_target = get_target(task_list.get(i), file_shell);
				if(main_target!=null){
					Monitor monitor = new Monitor(task_list.get(i).get_trigger(), task_list.get(i), file_shell, main_target,mySummary,myDetail);
					monitor.start();
				}
			}
			Thread summary_t = new Thread(mySummary);
			Thread detail_t = new Thread(myDetail);
			summary_t.start();
			detail_t.start();
			filedemo filedemo = new filedemo(file_shell);
			filedemo.start();
		}catch (Exception e) {}
	}
}
