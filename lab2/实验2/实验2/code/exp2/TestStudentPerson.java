package exp2;

public class TestStudentPerson {
	public static void main(String[] args)
	{
		Person[] person=new Person[4];
		
		
		person[0]=new Student("Jin",'M',20,"201504135146","061501");//添加一条学生信息
		person[0].register();//学生注册
	    person[0].updateAge(23);//更新该生的年龄
	    person[0].toString();//打印输出
	    
	    
	    person[1]=new Person("Kate",'F',21);//添加一条学生信息
	    Student stu=(Student)person[1];
	    stu.register();//学生注册
	    stu.updateAge(25);//更新该生的年龄
	    stu.toString();//打印输出
	    stu.printBasicInfo();//打印输出基本信息
	    
	    person[2]=new Teacher("Rene",'M',35,"06","01452");//添加一条教师信息
	    person[2].register();//完成教师的注册，记录注册时间并设定基本薪资
	    person[2].toString();
	    
	    person[3]=new Person("Jason",'M',41);//添加一条教师信息
	    Teacher te=(Teacher)person[3];
	    te.register();//学生注册
	    te.printDetailInfo();//打印输出
	    }
	    
	    
	    
	    
	}   
		
		
