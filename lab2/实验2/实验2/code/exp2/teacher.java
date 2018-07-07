package exp2;

import java.util.Date;

class Teacher extends Person  implements PrintInfo{
	public String departmentno,tno;
	double salary;
	public Date hiredate;
	
	
	public String printDetailInfo(){
        System.out.println(name+"  "+sex+"    "+age+"  "+tno+"     "+departmentno+"          "+hiredate);
        return name+"  "+sex+"    "+age+"  "+tno+"     "+departmentno+"          "+hiredate;
    				
	}
    private void register(){
    	this.salary =6000;
       	this.hiredate=new Date();
	    				
		}    
 }