package elevator;


/* Overview: elevator 类保存了电梯的运动方向和所处楼层信息 能够前往某一楼层 提供了返回电梯相关信息的方法
 * Objects: int floor, String direction
 * Abstraction Function: AF(c) = (floor, direction), where
 * 		floor = c.floor, direction = c.direction
 * Invariant: (direction!=null)
 * */
public class elevator implements abstract_elevator{
	private int floor;
	private String direction;

	public boolean repOk() {
		/*
		  @MODIFIES: None
		  @EFFECTS: (this.direction==null||this.floor<1||this.floor>10)==>\result == false
		  			(otherwise)==>\result==true
		  */
		if(direction==null)	return false;
		return true;
	}
	
	elevator(){
		/*
		  @MODIFIES: this
		  */
		floor = 1;
		direction = "STILL";
	}
	
	public void set_floor(int f){
		/*
		  @MODIFIES: this
		  @EFFECTS: (f>this.floor)==>(this.direction=="UP")&&(this.floor==f)
		  			(f<this.floor)==>(this.direction=="DOWN")&&(this.floor==f)
		  			(f==this.floor)==>(this.direction=="STILL")&&(this.floor==f)
		  */
		if(f>floor)
			direction = "UP";
		else if(f<floor)
			direction = "DOWN";
		else
			direction = "STILL";
		floor = f;
	}
	
	public int get_floor(){
		/*
		  @MODIFIES: None
		  @EFFECTS: \result==this.floor
		  */
		return floor;
	}
	
	public String get_direction(){
		/*
		  @MODIFIES: None
		  @EFFECTS: \result==this.direction
		  */
		return direction;
	}
	
	public String toString(double t){
		/*
		  @MODIFIES: None
		  @EFFECTS: \result=="("+floor+","+direction+","+t+")"
		  */
		String str = "("+floor+","+direction+","+t+")";
		return str;
	}
	
}
