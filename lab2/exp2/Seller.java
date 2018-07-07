package exp2;

public class Seller extends Marketer{
	private int Order_num;
	private double Sales;
	
	Seller(int id){
		super(id);
		Order_num = 0;
		Sales = 0.0;
	}
	
	public void Sales_update(){
		Order_num = 0;
		Sales = 0.0;
	}
	
	public void Punch_Card(double t){
		punish_num = 0;
	}
	
	public void Advertise_and_Sell(){
		System.out.println("Advertise and Sell");
	}
	
	public void get_Order(String product){
		Order_num++;
	}
	
	public void get_Money(double money,boolean f){
		if(f)	Sales += money;
	}
	
	public double getSalary() {
        if(Sales>300000)
        	return super.getSalary()+(Sales-300000)*0.2;
        else if(Sales<300000)
        	return (super.getSalary()-15000)*0.8;
        else
        	return super.getSalary();
    } 
}
