package Bank;

import java.util.*;

public class Account {
	//OVERVIEW: 银行账户 管理账户的相关信息 进行存钱取钱以及转账等相关操作
	/*
	 * */
	private String AccountId;
	private String UserId;
	private String name;
	private String pwd;
	private double balance=0.0;
	private boolean isActivate=true;
	private static int counter = 0;
		
		
    public Account(String id, String name, String pwd) {
    	/*@REQUIRES: 
		  @MODIFIES:this 
		  @EFFECTS: 
		  */
		this.AccountId = String.valueOf(counter++);
		this.UserId=id;
		this.name=name;
		this.pwd=pwd;
		this.isActivate=true;
	
		}
		
	public String getAccountId() {
		/*@REQUIRES: 
		  @MODIFIES: 
		  @EFFECTS: \result == this.AccountId; 
		  */
		return AccountId;
	}

	public void setAccountId(String accountId) {
		/*@REQUIRES: 
		  @MODIFIES: this 
		  @EFFECTS: 
		  */
		this.AccountId = accountId;
	}

	public String getUserId() {
		/*@REQUIRES: 
		  @MODIFIES: 
		  @EFFECTS: \result == this.UserId; 
		  */
		return UserId;
	}

	public void setUserId(String id) {
		/*@REQUIRES: 
		  @MODIFIES: this 
		  @EFFECTS: 
		  */
		this.UserId = id;
	}
	
	public String getName() {
		/*@REQUIRES: 
		  @MODIFIES: 
		  @EFFECTS: \result == this.name; 
		  */
		return name;
	}

	public void setName(String name) {
		/*@REQUIRES: 
		  @MODIFIES: this
		  @EFFECTS: 
		  */
		this.name = name;
	}
	public String getPwd() {
		/*@REQUIRES: 
		  @MODIFIES: 
		  @EFFECTS: \result == this.pwd;
		  */
		return pwd;
	}

	public void setPwd(String pwd) {
		/*@REQUIRES: 
		  @MODIFIES: this
		  @EFFECTS: 
		  */
		this.pwd = pwd;
	}

	
	public double getBalance() {
		/*@REQUIRES: 
		  @MODIFIES: 
		  @EFFECTS: \result == this.balance;
		  */
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean getIsActivate() {
		return isActivate;
	}

	public void setIsActivate(boolean isActivate) {
		this.isActivate = isActivate;
	}
	
		
	public boolean depositMoney(double money) {
		if (money <= 0) 	
			return false;
		this.balance += money;
		return true;
	}

	public boolean drawMoney(double money) {
		if (money <= 0) 	
			    return false;
		if(!this.getIsActivate()) return false;
		if (this.balance< money)
				return false;
		this.balance -= money;
	    return true;
	}
	
	public boolean transferMoney(String AccountId,String name,double money,AccountSet accounts) {
		if(!this.getIsActivate()) return false;
		Account target=accounts.queryByAccountId(AccountId);
		if(target!=null&&target.getName().equals(name))
		{
			this.balance -= money;
		target.setBalance(target.getBalance()+money);
	        return true;
		}
		return false;
	}
	
	public String toString(){
	  return ("AccountId:"+AccountId+"UserId:"+UserId+"Name:"+name+"isActivate:"+isActivate);
	}
	public boolean reqOk(){
		if(UserId==null||pwd==null) return false;
		return true;	
	}


}
