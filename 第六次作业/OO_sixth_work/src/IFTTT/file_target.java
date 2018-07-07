package IFTTT;

public class file_target {
	String type;
	String name;
	String parent_d;
	String path;
	long size;
	long modified_t;
	
	int compare;
	int renamed;
	
	file_target(String type_tmp,String name_tmp,String pd_tmp,String path_tmp,long size_tmp,long mt_tmp) {
		type = type_tmp;
		name = name_tmp;
		parent_d = pd_tmp;
		path = path_tmp;
		size = size_tmp;
		modified_t = mt_tmp;
		compare = 0;
		renamed = 0;
	}
	
	public boolean total_equals(file_target file){
		if(name.equals(file.name)&&parent_d.equals(file.parent_d)&&path.equals(file.path)&&size==file.size&&modified_t==file.modified_t)
			return true;
		return false;
	}
	public boolean equals(file_target file){
		if(path.equals(file.path)&&type.equals(file.type))
			return true;
		return false;
	}
	public boolean path_change_equals(file_target file){
		if(name.equals(file.name)&&size==file.size&&modified_t==file.modified_t&&type.equals(file.type)&&!parent_d.equals(file.parent_d))
			return true;
		return false;
	}
	
	public boolean renamed_equals(file_target file){
		if(parent_d.equals(file.parent_d)&&!name.equals(file.name)&&type.equals(file.type)&&size==file.size&&modified_t==file.modified_t)
			return true;
		return false;
	}
	
}
