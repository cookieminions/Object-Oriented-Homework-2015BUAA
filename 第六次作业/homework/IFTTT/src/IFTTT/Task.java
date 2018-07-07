package IFTTT;

import java.util.Vector;

public class Task {
	private String task_str;
	private String path;
	private String trigger;
	private String task;
	private Vector<String> tasks;
	
	Task(String str,String tmp_path,String tmp_trigger,String tmp_task){
		task_str = str;
		path = tmp_path;
		trigger = tmp_trigger;
		task = tmp_task;
		tasks = new Vector<String>();
		tasks.add(task);
	}
	public void addTask(String t){
		tasks.add(t);
	}
	public Vector<String> getTasks() {
		return tasks;
	}
	public String get_path() {
		return path;
	}
	public String get_trigger() {
		return trigger;
	}
	public String get_task() {
		return task;
	}
	public boolean equals(Task tmp_task) {
		if(tmp_task.get_path().equals(path)&&tmp_task.get_trigger().equals(trigger)&&tmp_task.get_task().equals(task))
			return true;
		else	return false;
	}
	public boolean trigger_equals(Task tmp_task) {
		if(tmp_task.get_trigger().equals(trigger)&&tmp_task.get_path().equals(path)&&!tmp_task.get_task().equals(task)){
			return true;
		}
		return false;
	}
	public String toString() {
		return task_str;
	}
}
