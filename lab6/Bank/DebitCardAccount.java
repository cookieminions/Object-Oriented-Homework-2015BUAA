package Bank;


public class DebitCardAccount extends Account {
	//OVERVIEW:储蓄帐户与年存款利率有关
	//存款时计算利息
    private double annualInterestRate;
    
    
    public DebitCardAccount(String id,String name,String pwd) {
    	super(id,name,pwd);
    	this.setBalance(10.0);
    	}
    
    public void changeStatus() {
    	if(this.getBalance()<10)
    		this.setIsActivate(false);
    	else 
    		this.setIsActivate(true);
    }
	
        //override
    public boolean depositMoney(double money) {
    	if (money <= 0) 	
			return false;    	
    	double balance=this.getBalance();
		balance += money*(1+annualInterestRate);
		this.setBalance(balance);
		changeStatus();
		return true;
    
    	}
    //override   
    public boolean drawMoney(double money,String pwd) {
    	if (money <= 0) 	
			    return false;
    	if(!this.getIsActivate()) return false;
       	if(!(this.getPwd().equals(pwd))) return false;
		if (this.getBalance()-10< money)
				return false;
		this.setBalance(this.getBalance()-money);
	    return true;
	}
    
    public boolean transferMoney(String AccountId,String name,double money,AccountSet accounts) {
    	if (this.getBalance()-10< money)
				return false;
    	else 
			return super.transferMoney(AccountId, name, money, accounts);
    }
    
	
    
    public void setRate(double interestRate){
  		this.annualInterestRate=interestRate;
  	}
  	
  	public double getRate()
  	{
  		return this.annualInterestRate;
  	}
  	public boolean reqOk(){
  		if(!super.reqOk())	return false;
  		
  		return true;
  	}
  	
}

