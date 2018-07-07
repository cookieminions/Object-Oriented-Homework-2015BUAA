package elevator;

public class elevator implements abstract_elevator{
	private int floor;
	private String direction;
	
	elevator(){
		floor = 1;
		direction = "STILL";
	}
	
	public void set_floor(int f){
		if(f>floor)
			direction = "UP";
		else if(f<floor)
			direction = "DOWN";
		else
			direction = "STILL";
		floor = f;
	}
	
	public int get_floor(){
		return floor;
	}
	
	public String get_direction(){
		return direction;
	}
	
	public String toString(double t){
		String str = "("+floor+","+direction+","+t+")";
		return str;
	}
	
}
