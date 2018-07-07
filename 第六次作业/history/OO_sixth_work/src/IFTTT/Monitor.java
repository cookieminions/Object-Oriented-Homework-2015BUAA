package IFTTT;

import java.util.Vector;


public class Monitor extends Thread{
	private Task task;
	private String trigger;
	private Vector<String> tasks;
	private summary mySummary;
	private detail myDetail;
	private boolean runnable;
	
	private FileList file_shell;
	private snapshot lastsnapshot;
	private snapshot currsnapshot;
	private file_target main_target;
	
	Monitor(String tmp_trigger,Task tmp_task,FileList filelist,file_target tmp_target,summary tmp_summary,detail tmp_detail){
		trigger = tmp_trigger;
		task = tmp_task;
		file_shell = filelist;
		main_target = tmp_target;
		lastsnapshot = new snapshot();
		currsnapshot = new snapshot();
		mySummary = tmp_summary;
		myDetail = tmp_detail;
		tasks = task.getTasks();
		runnable = true;
	}
	
	public void run(){
		if(main_target.type.equals("File"))
			monitor(currsnapshot.root, main_target.parent_d);
		else
			monitor(currsnapshot.root, main_target.path);
		update();
		System.out.println(main_target.path);
		/*if(lastsnapshot.root!=null)
			System.out.println(lastsnapshot.root.target.path);*/
		file_node main_node = lastsnapshot.search(lastsnapshot.root,main_target);
		if(main_node!=null){
			main_target = main_node.target;
		}else{
			System.out.println("INVALID "+task.get_path());
			runnable = false;
		}
		
		
		while(runnable){
			if(main_target.type.equals("File"))
				monitor(currsnapshot.root, main_target.parent_d);
			else
				monitor(currsnapshot.root, main_target.path);
			check_trigger();
			update();
			//file_shell.refresh();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void monitor(file_node root,String path){
		safe_file file = file_shell.get_shell(path);
		
		String name_tmp = file.getName();String pd_tmp = file.getParent();String path_tmp = file.getPath();
		long mt_tmp = file.lastModified();long size_tmp = file.getlength();
		String type_tmp = file.isDirectory()?"Directory":"File";
		file_target target = new file_target(type_tmp, name_tmp, pd_tmp, path_tmp, size_tmp, mt_tmp);
		//System.out.println(type_tmp+" "+name_tmp+" "+pd_tmp+" "+path_tmp+" "+size_tmp+" "+mt_tmp);
		
		String[] filelist = file.filelist();
		
		file_node target_node = new file_node(target);
		currsnapshot.add_node(root, target_node);
		
		if(filelist!=null){
			for(int i=0;i<filelist.length;i++){
				//System.out.println(filelist[i]);
				monitor(target_node,filelist[i]);
			}
		}
	}
	
	public void check_trigger(){
		if(trigger.equals("renamed")){
			if(main_target.type.equals("File")){
				file_target last_target = lastsnapshot.search(lastsnapshot.root, main_target)!=null?lastsnapshot.search(lastsnapshot.root, main_target).target:null;
				file_target curr_target = currsnapshot.search(currsnapshot.root, main_target)!=null?currsnapshot.search(currsnapshot.root, main_target).target:null;
				if(last_target!=null&&curr_target==null){
					file_node renamed_node = currsnapshot.renamed_search(currsnapshot.root, main_target);
					if(renamed_node!=null){
						file_node last_node = lastsnapshot.search(lastsnapshot.root, renamed_node.target);
						if(last_node==null){
							work(last_target,renamed_node.target);
							main_target = renamed_node.target;
						}
					}
				}
				
			}else if(main_target.type.equals("Directory")){
				renamed_compare(main_target);
			}
		}
		else if(trigger.equals("Modified")){
			file_target last_target = lastsnapshot.search(lastsnapshot.root, main_target)!=null?lastsnapshot.search(lastsnapshot.root, main_target).target:null;
			file_target curr_target = currsnapshot.search(currsnapshot.root, main_target)!=null?currsnapshot.search(currsnapshot.root, main_target).target:null;
			if((curr_target!=null&&last_target!=null&&curr_target.modified_t!=last_target.modified_t&&main_target.type.equals("File"))){
				work(last_target,curr_target);
				if(curr_target!=null)	main_target = curr_target;
			}
			
			if(last_target!=null&&curr_target!=null&&
					main_target.type.equals("Directory")){
				file_node last_node = lastsnapshot.search(lastsnapshot.root, main_target);
				file_node curr_node = currsnapshot.search(currsnapshot.root, main_target);
				modified_compare(last_node, curr_node);
				main_target = curr_target;
			}
		}
		else if(trigger.equals("path-changed")){
			if(main_target.type.equals("File")){
				file_target last_target = lastsnapshot.search(lastsnapshot.root, main_target)!=null?lastsnapshot.search(lastsnapshot.root, main_target).target:null;
				file_target curr_target = currsnapshot.search(currsnapshot.root, main_target)!=null?currsnapshot.search(currsnapshot.root, main_target).target:null;
				
				if(curr_target==null&&last_target!=null){
					file_node path_change_node = currsnapshot.path_change_search(currsnapshot.root, main_target);
					if(path_change_node!=null){
						file_node last_node = lastsnapshot.search(lastsnapshot.root, path_change_node.target);
						if(last_node==null){//如果发现的路径移动文件是原来存在的 是不算的
							work(last_target,path_change_node.target);
							main_target = path_change_node.target;
						}
					}
				}
			}else if(main_target.type.equals("Directory")){
				path_change_compare(main_target);
			}
		}
		else if(trigger.equals("size-changed")){
			file_target last_target = lastsnapshot.search(lastsnapshot.root, main_target)!=null?lastsnapshot.search(lastsnapshot.root, main_target).target:null;
			file_target curr_target = currsnapshot.search(currsnapshot.root, main_target)!=null?currsnapshot.search(currsnapshot.root, main_target).target:null;
			
			if((curr_target!=null&&last_target!=null&&curr_target.size!=last_target.size&&main_target.type.equals("File"))||
					(curr_target!=null&&last_target==null)||(curr_target==null&&last_target!=null)){
				work(last_target,curr_target);
				if(curr_target!=null)	main_target = curr_target;
			}
			if(last_target!=null&&curr_target!=null&&
					main_target.type.equals("Directory")){
				file_node last_node = lastsnapshot.search(lastsnapshot.root, main_target);
				file_node curr_node = currsnapshot.search(currsnapshot.root, main_target);
				size_change_compare(last_node,curr_node);
				size_change_iter(lastsnapshot.root, 0);
				size_change_iter(currsnapshot.root, 1);
				main_target = curr_target;
			}
		}
		
	}
	
	public void work(file_target last_file,file_target curr_file) {
		System.out.println("work");
		for(int i=0;i<tasks.size();i++){
			String need = tasks.get(i);
			//System.out.println(need+" "+trigger+" "+tasks.size());
			if(need.equals("record-summary")){
				mySummary.record(trigger);
			}
			else if(need.equals("record-detail")){
				myDetail.record(trigger, last_file, curr_file, main_target);
			}
			else if(need.equals("recover")){
				recover(last_file,curr_file);
			}
		}
	}
	
	public void recover(file_target last_file,file_target curr_file) {
		if(trigger.equals("renamed")){
			safe_file tmp_file = file_shell.get_shell(curr_file.path);
			String oldname = last_file.name;
			file_shell.rename(tmp_file,oldname);
			file_node tmp_node = currsnapshot.search(currsnapshot.root, curr_file); 
			tmp_node.target.name = oldname;
			tmp_node.target.path = tmp_node.target.parent_d+"\\"+oldname;
		}
		else if(trigger.equals("path-changed")){
			safe_file tmp_file = file_shell.get_shell(curr_file.path);
			String oldparent = last_file.parent_d;
			file_shell.moveTo(tmp_file, oldparent);
			file_node tmp_node = currsnapshot.search(currsnapshot.root, curr_file); 
			tmp_node.target.parent_d = oldparent;
			tmp_node.target.path = last_file.path;
		}
	}
	
	public void update() {
		lastsnapshot.root = currsnapshot.root;
		currsnapshot.root = null;
	}

	public void modified_compare(file_node last_node,file_node curr_node){
		if(last_node.target.equals(curr_node.target)){
			if(last_node.target.modified_t!=curr_node.target.modified_t){
				work(last_node.target,curr_node.target);
			}
		}
		for(int i=0;i<last_node.child_nodelist.size();i++){
			for(int j=0;j<curr_node.child_nodelist.size();j++){
				modified_compare(last_node.child_nodelist.get(i), curr_node.child_nodelist.get(j));
			}
		}
	}
	public void size_change_compare(file_node last_node,file_node curr_node){
		if(last_node.target.equals(curr_node.target)){
			last_node.target.compare = 2;
			curr_node.target.compare = 1;
			if(last_node.target.size!=curr_node.target.size){
				work(last_node.target,curr_node.target);
			}
		}
		for(int i=0;i<last_node.child_nodelist.size();i++){
			for(int j=0;j<curr_node.child_nodelist.size();j++){
				size_change_compare(last_node.child_nodelist.get(i), curr_node.child_nodelist.get(j));
			}
		}
	}
	public void size_change_iter(file_node root,int time){
		if(time==0&&root.target.compare!=2){
			work(root.target, null);
		}
		if(time==1&&root.target.compare!=1){
			work(null, root.target);
		}
		for(int i=0;i<root.child_nodelist.size();i++){
			size_change_iter(root.child_nodelist.get(i), time);
		}
	}
	
	
	
	public void path_change_compare(file_target target){
		if(target.type.equals("File")){
			file_target last_target = lastsnapshot.search(lastsnapshot.root, target)!=null?lastsnapshot.search(lastsnapshot.root, target).target:null;
			file_target curr_target = currsnapshot.search(currsnapshot.root, target)!=null?currsnapshot.search(currsnapshot.root, target).target:null;
			
			if(curr_target==null&&last_target!=null){
				file_node path_change_node = currsnapshot.path_change_search(currsnapshot.root, target);
				if(path_change_node!=null){
					file_node last_node = lastsnapshot.search(lastsnapshot.root, path_change_node.target);
					if(last_node==null)
						work(last_target,path_change_node.target);
				}
			}
		}else if(target.type.equals("Directory")){
			file_node target_node = lastsnapshot.search(lastsnapshot.root, target);
			for(int i=0;i<target_node.child_nodelist.size();i++){
				path_change_compare(target_node.child_nodelist.get(i).target);
			}
		}	
	}
	
	public void renamed_compare(file_target target){
		if(target.type.equals("File")){
			file_target last_target = lastsnapshot.search(lastsnapshot.root, target)!=null?lastsnapshot.search(lastsnapshot.root, target).target:null;
			file_target curr_target = currsnapshot.search(currsnapshot.root, target)!=null?currsnapshot.search(currsnapshot.root, target).target:null;
			
			if(curr_target==null&&last_target!=null){
				file_node renamed_node = currsnapshot.renamed_search(currsnapshot.root, target);
				if(renamed_node!=null){
					file_node last_node = lastsnapshot.search(lastsnapshot.root, renamed_node.target);
					if(last_node==null)//这一次找到的renamed结点必须是上一次不存在的
						work(last_target,renamed_node.target);
				}
			}
		}else if(target.type.equals("Directory")){
			file_node target_node = lastsnapshot.search(lastsnapshot.root, target);
			for(int i=0;i<target_node.child_nodelist.size();i++){
				renamed_compare(target_node.child_nodelist.get(i).target);
			}
		}
		
	}
	
	
}
