package exp2;

import java.util.Date;

class Student extends Person implements PrintInfo{
		public String classno,sno;//学号和班号
		public Date registerdate;//注册时间
		
		public void updateAge(int age){
			this.age=age;
		}
	    
		public void register() {
	    	this.registerdate=new Date();
	    }
	    
		public String  printBasicInfo(){//打印输出学生基本信息
	        System.out.println(name+"  "+sex+"    "+age+"  "+sno+"     "+classno);
	        return name+"  "+sex+"    "+age+"  "+sno+"     "+classno;
	    				
		}
		
		Student(String name,char sex,int age,String sno,String classno){
			 this.name=name;
			 this.sex=sex;
			 this.age=age;
			 this.sno=sno;
			 this.classno=classno;
		}
		
	}
	
	
	

