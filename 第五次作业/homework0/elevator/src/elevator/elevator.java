package elevator;

public class elevator extends Thread{
	private String direction;
	private int floor;
	private String status;
	private int move;
	private int id;
	
	private request main_req;
	private int turn_flag;
	
	elevator(int i) {
		id = i;
		floor = 1;
		status = "STILL";
		direction = "STILL";
		turn_flag = 0;
		move = 0;
		main_req = null;
		//System.out.println("move");
	}
	
	public void run() {
		while(true){
			int flag = 0;
			if(main_req!=null){
				while(floor!=main_req.get_des_floor()){
					if(floor<main_req.get_des_floor())	{
						direction = "UP";
						moveup();
					}
					else if(floor>main_req.get_des_floor()){
						direction = "DOWN";
						movedown();
					}
					else{
						direction = "STILL";
					}
					
					if(turn_flag==1){
						turnon();
						turnoff();
					}
					flag = 1;
				}
				if(floor==main_req.get_des_floor()){
					if(flag!=1){
						turn_flag = 1;
						turnon();
						//System.out.println("open "+floor);
						turnoff();
					}
					main_req = null;
				}
			}
			yield();
		}
	}
	
	public void moveup() {
		status = "UP";
		//System.out.println(id + " move up");
		try {
			sleep(3000);
		} catch (InterruptedException e) {
		}
		status = "MOVE";
		floor++;
		move++;
	}
	public void movedown() {
		status = "DOWN";
		//System.out.println(id + " move down");
		try {
			sleep(3000);
		} catch (InterruptedException e) {
		}
		status = "MOVE";
		floor--;
		move++;
	}
	public void turnon() {
		status = "STILL";
		//System.out.println(id + " turn on "+floor);
		try {
			sleep(6000);
		} catch (InterruptedException e) {
		}
	}
	public void turnoff() {
		//status = "MOVE";
		//System.out.println(id + " turn off");
		turn_flag = 0;
	}
	
	public int get_floor() {
		return floor; 
		/*status.equals("STILL")?floor:(direction.equals("UP")?floor+1:floor-1);//*/
	}
	public int get_move() {
		return move;
	}
	public String get_direction() {
		return direction;
	}
	public String get_status() {
		return status;
	}
	public int get_aim_floor() {
		return main_req.get_des_floor();
	}
	public request get_main_req() {
		return main_req;
	}
	public void set_main_req(request req) {
		main_req = req;
	}
	public void set_flag(int i) {
		turn_flag = i;
	}
	public int get_flag() {
		return turn_flag;
	}
	
}
