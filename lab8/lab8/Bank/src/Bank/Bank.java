package Bank;

import java.util.Vector;

public class Bank {
	private Vector<Account> AccountSet;
	public Bank() {
		AccountSet = new Vector<Account>();
	}
	
	public Account createAccount(String name,String number, String password) {
		return new Account(name, number, password);
	}
	
	public void RelateAccount(String number) {
		
	}
	
	public void transferMoney(double Money,long AccountNumber1,long AccountNumber2) {
		
	}
}
