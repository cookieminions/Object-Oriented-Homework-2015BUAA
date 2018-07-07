package IFTTT;

import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Handle {
	private static Scanner s_in;
	private Vector<Task> task_list;
	
	Handle() {
		task_list = new Vector<Task>();
	}
	
	public void input_handle() {
		s_in = new Scanner(System.in);
		String line = "";
		while(!((line = s_in.nextLine()).equals("start"))){
			Task task = check_task(line);
			if(task==null){
				System.out.println("INVALID "+line);
			}
			else{
				if(check(task))
					task_list.add(task);
			}
		}
		s_in.close();
	}
	
	public Task check_task(String str){
		Task task = null;
		String regEx1 = "IF \\[(.*)\\] (renamed|Modified|path-changed|size-changed) THEN (record-summary|record-detail)";
		String regEx2 = "IF \\[(.*)\\] (renamed|path-changed) THEN (recover)";
		
		if(str.matches(regEx1)){
			Pattern pattern = Pattern.compile(regEx1);
			Matcher matcher = pattern.matcher(str);
			if(matcher.find()){
				String path = matcher.group(1);
				String trigger = matcher.group(2);
				String taskstr = matcher.group(3);
				task = new Task(str, path, trigger, taskstr);
				return task;
			}
		}
		if(str.matches(regEx2)){
			Pattern pattern = Pattern.compile(regEx2);
			Matcher matcher = pattern.matcher(str);
			if(matcher.find()){
				String path = matcher.group(1);
				String trigger = matcher.group(2);
				String taskstr = matcher.group(3);
				task = new Task(str, path, trigger, taskstr);
				return task;
			}
		}
		return task;
	}
	
	
	public boolean check(Task task){
		for(int i=0;i<task_list.size();i++){
			Task tmp_task = task_list.get(i);
			if(tmp_task.trigger_equals(task)){
				tmp_task.addTask(task.get_task());
				return false;
			}
			if(tmp_task.equals(task))
				return false;
		}
		return true;
	}
	
	public Vector<Task> get_tasklist(){
		return task_list;
	}
	
}
