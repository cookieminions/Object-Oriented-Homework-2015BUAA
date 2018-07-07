package exp2;

public class Marketer extends Employee{
	
	Marketer(int id){
		super(id);
	}
	
	public double getSalary(){
		return super.getSalary() + 15000;
	}
	
	 public String getReimbursementForm() {
	        return "pink";     // use the pink form
	 }
	 
	 public void Collect_Infor(){
		 System.out.println("Collect Information");
	 }
}
