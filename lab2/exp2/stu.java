package exp2;

import java.util.Date;

class Student extends Person implements PrintInfo{
		public String classno,sno;//ѧ�źͰ��
		public Date registerdate;//ע��ʱ��
		
		public void updateAge(int age){
			this.age=age;
		}
	    
		public void register() {
	    	this.registerdate=new Date();
	    }
	    
		public String  printBasicInfo(){//��ӡ���ѧ��������Ϣ
	        System.out.println(name+"  "+sex+"    "+age+"  "+sno+"     "+classno);
	        return name+"  "+sex+"    "+age+"  "+sno+"     "+classno;
	    				
		}
		
		public String printDetailInfo(){//
	        System.out.println(name+"  "+sex+"    "+age+"  "+sno+"     "+classno+"          "+registerdate);
	        return name+"  "+sex+"    "+age+"  "+sno+"     "+classno+"          "+registerdate;
	    				
		}
		
		Student(String name,char sex,int age,String sno,String classno){
			super(name, sex, age);	//
			this.name=name;
			this.sex=sex;
			this.age=age;
			this.sno=sno;
			this.classno=classno;
		}
		
	}
	
	
	
