package taxi;

/* Overview: 这个类保存了请求的发出地与目的地以及请求发出时间等信息 提供了判断请求是否相同的方法
 * Objects: req_str, start_point, end_point, req_t;
 * request类提供了equals方法比较两个请求是否相等
 * request类提供了reqInfor方法得到需要的请求格式字符串
 * invariant: !((req_str==null||start_point==null||end_point==null||!((Object)req_t instanceof Double)))
 */
public class request {
	String req_str;
	point start_point;
	point end_point;
	double req_t;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (req_str==null||start_point==null||end_point==null||!((Object)req_t instanceof Double)) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(req_str==null||start_point==null||end_point==null||!((Object)req_t instanceof Double))
			return false;
		return true;
	}
	
	public request(String str,int start_x,int start_y,int end_x,int end_y,double t) {
		/*
		  @MODIFIES: req_str, start_point, end_point, req_t;
		  */
		req_str = str;
		req_t = t;
		
		start_point = new point();
		start_point.x = start_x; start_point.y = start_y;
		end_point = new point();
		end_point.x = end_x; end_point.y = end_y;
	}

	public boolean equals(request r) {
		/*@REQUIRES: r!=null; 
		  @MODIFIES: None;
		  @EFFECTS: (start_point.equals(r.start_point)&&end_point.equals(r.end_point)&&req_t==r.req_t) ==> \result == true;
		  			(!(start_point.equals(r.start_point)&&end_point.equals(r.end_point)&&req_t==r.req_t)) ==> \result == false;
		  */
		if(start_point.equals(r.start_point)&&end_point.equals(r.end_point)&&req_t==r.req_t)
			return true;
		return false;
	}
	
	public String toString() {
		/*
		  @MODIFIES: None;
		  @EFFECTS: \result == "CR-("+start_point.x+" "+start_point.y+")"+"To("+end_point.x+" "+end_point.y+")-"+req_t;
		  */
		String str = "CR-("+start_point.x+" "+start_point.y+")"+"To("+end_point.x+" "+end_point.y+")-"+req_t;
		return str;
	}
	
	public String reqInfor() {
		/*
		  @MODIFIES: None;
		  @EFFECTS: \result == "reqTime: "+req_t+"  start point: "+start_point.toString()+"  end point: "+end_point.toString()+"\n";
		  */
		String str = "reqTime: "+req_t+"  start point: "+start_point.toString()+"  end point: "+end_point.toString()+"\n";
		return str;
	}
	
}
