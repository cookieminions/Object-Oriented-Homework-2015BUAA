package Bank;

import java.util.ArrayList;
import java.util.Iterator;

public class test {

	/**
	 * @param args
	 */
	public static AccountSet accountset = new AccountSet();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Account a1=new Account("01","jane","123");
		accountset.insert(a1);
		Account a2=new Account("02","greece","123");
		accountset.insert(a2);
		
		
		CreditCardAccount C_a= new CreditCardAccount("03", "stockph", "123456");
		DebitCardAccount D_a = new DebitCardAccount("04", "jack", "123");
		
		System.out.println("-----------Test DebitCardAccount-------------");
		//deposit money
		if(D_a.depositMoney(500.0)){
			System.out.println(D_a.getName()+" deposit money 500.0 success");
		}else System.out.println(D_a.getName()+" deposit money 500.0 fail");
		//draw money
		if(D_a.drawMoney(200.0,"123")){
			System.out.println((D_a.getName()+" draw money 200.0 success"));
		}
		if(D_a.drawMoney(200.0,"12345")){
			System.out.println((D_a.getName()+" draw money 200.0 success"));
		}else	System.out.println((D_a.getName()+" draw money 200.0 fail"));
		//transfor money
		if(D_a.transferMoney("02", "greece", 20, accountset)){
			System.out.println("transfer money to greece 20 success");
		}else System.out.println("transfer money to greece 20 fail");
		if(D_a.transferMoney("02", "jane", 20, accountset)){
			System.out.println("transfer money to jane 20 success");
		}else System.out.println("transfer money to jane 20 fail");
		if(D_a.transferMoney("02", "greece", 1000, accountset)){
			System.out.println("transfer money to greece 1000 success");
		}else System.out.println("transfer money to greece 1000 fail");
		
		System.out.println("-----------Test CreditCardAccount-------------");
		
		C_a.setMax(500.0);
		System.out.println(C_a.getName()+" setMax 500.0 success");
		//deposit money
		if(C_a.depositMoney(500.0)){
			System.out.println(C_a.getName()+" deposit money 500.0 success");
		}else System.out.println(C_a.getName()+" deposit money 500.0 fail");
		//draw money
		if(C_a.drawMoney(200.0)){
			System.out.println((D_a.getName()+" draw money 200.0 success"));
		}
		if(C_a.drawMoney(900.0)){
			System.out.println((D_a.getName()+" draw money 900.0 success"));
		}else	System.out.println((D_a.getName()+" draw money 900.0 fail"));
		//transfor money
		if(C_a.transferMoney("02", "greece", 20, accountset)){
			System.out.println("transfer money to greece 20 success");
		}else System.out.println("transfer money to greece 20 fail");
		if(C_a.transferMoney("02", "jane", 20, accountset)){
			System.out.println("transfer money to jane 20 success");
		}else System.out.println("transfer money to jane 20 fail");
		if(C_a.transferMoney("02", "greece", 1000, accountset)){
			System.out.println("transfer money to greece 1000 success");
		}else System.out.println("transfer money to greece 1000 fail");
		
		System.out.println("-----------Test AccountSet-------------");
		
		Account a3 = new CreditCardAccount("01", "jane", "123456");
		Account a4 = new DebitCardAccount("01", "jane", "123456");
		accountset.insert(a3);
		accountset.insert(a4);
		
		System.out.println(a1.getName()+"'s AccountId is "+a1.getAccountId());
		System.out.println("use AccountId search UserId");
		System.out.println(a1.getName()+"'s UserId is "+accountset.queryByAccountId(a1.getAccountId()).getUserId());
		
		AccountSet tmp = accountset.queryByUserId(accountset.queryByAccountId(a1.getAccountId()).getUserId());
		System.out.println(a1.getName()+"has"+tmp.size()+" accounts");
		String accountids = "";
		Iterator<Account> iter = tmp.elements();
		while(iter.hasNext()){
			accountids += iter.next().toString()+" ";
		}
		System.out.println("And they are "+accountids);
		
		System.out.println("before remove accounttest's size "+accountset.size());
		try {
			accountset.removeByAccountId(a1.getAccountId());
		} catch (InvalidAccountException e) {
			e.printStackTrace();
		}
		System.out.println("after remove accounttest's size "+accountset.size());
		
	
	}

}
