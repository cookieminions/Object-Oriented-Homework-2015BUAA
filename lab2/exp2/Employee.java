package exp2;

public class Employee {
		protected int id;//
		protected String Towork_time;//
		protected String Offwork_time;//
		protected int punish_num;
		protected double add_Salary;
		
		Employee(int id){
			this.id = id;
			this.add_Salary = 0;
			Towork_time = "8:30";
			Offwork_time = "17:30";
		}
		
		public void Punch_Card(double t){
			System.out.println("Punch Card at " + t);
			if(t>8.30 && t<17.30)
				punish_num ++;
		}//
		
		public void punish_num_update(){
			punish_num = 0;
		}
		
		public void Salary_Update(boolean f){
			if(f){
				add_Salary = (this.getSalary()+add_Salary)*1.02-this.getSalary();
			}		
		}
		
		public String submmit(){
			String account = new String();
			return account;
		}
		
	    public int getHours() {
	        return 40;           // works 40 hours / week
	    }
	    
	    public double getSalary() {
	        return 50000.0 + add_Salary - punish_num*200;      // RMB50,000.00 / year
	    }
	    
	    public int getVacationDays() {
	        return 10;           // 2 weeks' paid vacation
	    }

	    public String getReimbursementForm() {
	    	
	        return "yellow";     // use the yellow form
	    }
	    public String sign(String name){
    		return name;      // signature for the bill
    		
    	}
}



