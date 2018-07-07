package elevator;

public class request {
	private String[] req;
	private String req_str;
	private String type;
	
	private int des_floor;
	
	private double req_t;
	
	request(String str,double t){
		req_t = t;
		req_str = str;
		req = str.split("[(),]");
		type = req[1];
		des_floor = (type.equals("FR"))?Integer.parseInt(req[2]):Integer.parseInt(req[3]);
	}
	public String get_type() {
		return type;
	}
	public String get_direction(){
		return ((type.equals("FR"))?req[3]:"ER");
	}
	public int get_ele_num(){
		/*int i = (type.equals("ER"))?(req[2].charAt(1)-'0'):0;
		System.out.println("ER #"+i);*/
		return (type.equals("ER"))?(req[2].charAt(1)-'0'):0;
	}
	public int get_des_floor() {
		return des_floor;
	}
	public double get_req_t() {
		return req_t;
	}
	public String toString() {
		return req_str;
	}
	public boolean equal(request req) {
		if(req.get_des_floor()==des_floor&&req.get_direction().equals(this.get_direction())){
			return true;
		}
		else return false;
	}

}
