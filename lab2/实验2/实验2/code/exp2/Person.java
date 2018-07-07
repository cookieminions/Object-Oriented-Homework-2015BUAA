package exp2;

class Person{
	public String name;
	public char sex;
	public int age;
	
	Person(String name,char sex,int age){
		this.name=name;
		this.sex=sex;
		this.age=age;
		
	}
	
	public void register(){
	    System.out.println(name+"  is registered successfull!  ");
	}
	
	
	public String toString(){
        System.out.println(name+"  "+sex+"    "+age);
    				return name+"  "+sex+"    "+age;
	}

}