package taxi;

public class request {
	String req_str;
	point start_point;
	point end_point;
	double req_t;
	
	public request(String str,int start_x,int start_y,int end_x,int end_y,double t) {
		req_str = str;
		req_t = t;
		
		start_point = new point();
		start_point.x = start_x; start_point.y = start_y;
		end_point = new point();
		end_point.x = end_x; end_point.y = end_y;
	}

	public boolean equals(request r) {
		if(start_point.equals(r.start_point)&&end_point.equals(r.end_point)&&req_t==r.req_t)
			return true;
		return false;
	}
	
	public String toString() {
		String str = "CR-("+start_point.x+" "+start_point.y+")"+"To("+end_point.x+" "+end_point.y+")-"+req_t;
		return str;
	}
	
}
