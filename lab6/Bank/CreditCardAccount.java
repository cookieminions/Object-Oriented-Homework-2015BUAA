package Bank;


public class CreditCardAccount extends Account{
	//OVERVIEW:信用卡帐户有最高透支额度
	//消费不能超出额度
		private double overdraftLimit;
	
	  	public CreditCardAccount(String id,String name,String pwd){
	  		super(id,name,pwd);
	  		this.setBalance(overdraftLimit);
	  	}
	  	
	  	public void setMax(double money){
	  		this.overdraftLimit=money;
	  	}
	  	
	  	public double getMax()
	  	{
	  		return this.overdraftLimit;
	  	}
	 
	  	
		@Override
        public boolean drawMoney(double money) {
			double balance=this.getBalance();
			if(!this.getIsActivate()) return false;
	  		if (balance + this.overdraftLimit < money)
	  			return false;
			this.setBalance(balance-money);
			return true;
		}
		
		@Override
		public boolean transferMoney(String AccountId,String name,double money,AccountSet accounts) {
			if(this.getBalance()<0) return false;
			else if(money-this.getBalance()>this.overdraftLimit) return false;
			else 
				return super.transferMoney(AccountId, name, money, accounts);
	    }
		
		public boolean reqOk(){
			if(!super.reqOk())	return false;
			
			return true;
		}
}