package exp2;

public class Developer extends Employee{
	protected int work_year;
	
	Developer(int id, int work_year){
		super(id);
		this.work_year = work_year;
	}
	
	public void work_year_update(){
		work_year ++;
	}
	
	public double getSalary(){
		return super.getSalary() + work_year*1000;
	}
	
	public void Research(String request){
		System.out.println("Research and Development "+request);
	}
}
