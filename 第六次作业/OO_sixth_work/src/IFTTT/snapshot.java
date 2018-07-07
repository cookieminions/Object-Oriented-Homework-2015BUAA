package IFTTT;

import java.util.Vector;

public class snapshot {
	file_node root = null;
	
	public void add_node(file_node node,file_node new_node) {
		if(node==null){
			if(root==null){
				root = new_node;
			}
		}else{
			node.child_nodelist.add(new_node);
			new_node.parent_node = node;
			node.target.size += new_node.target.size;
		}
	}
	
	public file_node search(file_node input,file_target target) {
		file_node tmp = null;
		if(input.target.equals(target)){
			return input;
		}
		
		for(int i=0;i<input.child_nodelist.size();i++){
			tmp = search(input.child_nodelist.get(i), target);
			if(tmp!=null)	break;
		}
		return tmp;
	}
	
	public file_node path_change_search(file_node input,file_target target) {
		file_node tmp = null;
		if(input.target.path_change_equals(target))	return input;
		
		for(int i=0;i<input.child_nodelist.size();i++){
			tmp = path_change_search(input.child_nodelist.get(i), target);
			if(tmp!=null)	break;
		}
		return tmp;
	}
	
	public file_node renamed_search(file_node input,file_target target) {
		file_node tmp = null;
		if(input.target.renamed_equals(target))	{
			if(input.target.renamed!=1){
				input.target.renamed=1;
				return input;
			}
		}
		
		for(int i=0;i<input.child_nodelist.size();i++){
			tmp = renamed_search(input.child_nodelist.get(i), target);
			if(tmp!=null)	break;
		}
		return tmp;
	}
	
}

class file_node{
	file_target target;
	file_node parent_node;
	Vector<file_node> child_nodelist;
	
	file_node(file_target t){
		target = t;
		parent_node = null;
		child_nodelist = new Vector<file_node>();
	}
	
}
