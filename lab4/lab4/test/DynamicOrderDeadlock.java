package test;

import java.util.concurrent.atomic.*;

public class DynamicOrderDeadlock {
	public static void transferMoney2(Account fromAccount,Account toAccount,DollarAmount amount)
            throws InsufficientFundsException {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                    System.out.println("No Deadlock");
                    System.out.println("from "+fromAccount.getAcctNo()+" "+fromAccount.getAmount());
					System.out.println("to "+toAccount.getAcctNo()+" "+toAccount.getAmount());
                }
    }
	public static void transferMoney(Account fromAccount,Account toAccount,DollarAmount amount)
			throws InsufficientFundsException {
		synchronized (fromAccount) {
			synchronized (toAccount) {
				if (fromAccount.getBalance().compareTo(amount) < 0)
					throw new InsufficientFundsException();
				else {
					fromAccount.debit(amount);
					toAccount.credit(amount);
					System.out.println("from "+fromAccount.getAcctNo()+" "+fromAccount.getAmount());
					System.out.println("to "+toAccount.getAcctNo()+" "+toAccount.getAmount());
				}
			}
		}
	}

	

    static class DollarAmount implements Comparable<DollarAmount> {
        // Needs implementation
    	int myAmount;

        public DollarAmount(int amount) {
        	myAmount = amount;
        }

        public DollarAmount add(DollarAmount d) {
        	myAmount += d.myAmount;
            return this;
        }

        public DollarAmount subtract(DollarAmount d) {
        	myAmount -= d.myAmount;
            return this;
        }

        public int compareTo(DollarAmount dollarAmount) {
            return 0;
        }
    }

    static class Account{
        private DollarAmount balance;
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            acctNo = sequence.incrementAndGet();
            balance = new DollarAmount(500);
        }

        /*synchronized*/ void debit(DollarAmount d) {//正确结果应该取消 synchronized 的注释
            balance = balance.subtract(d);
        }

        /*synchronized*/ void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        /*synchronized*/ DollarAmount getBalance() {
            return balance;
        }
        /*synchronized*/ int getAmount(){
        	return balance.myAmount;
        }

        /*synchronized*/ int getAcctNo() {
            return acctNo;
        }
    }
    static class testT extends Thread{
    	Account a1;
    	Account a2;
    	testT(Account a,Account b){
    		a1 = a;
    		a2 = b;
    	}
    	public void run() {
    		while(true){
    			try {
    				transferMoney(a1, a2, new DollarAmount(100));//此处为死锁
        			//transferMoney2(a1,a2,new DollarAmount(100));//此处为正确结果
        		} catch (InsufficientFundsException e) {
        		}
    		}
		}
    }
    
    

    static class InsufficientFundsException extends Exception {
    }
    
    public static void main(String[] args) {
		Account a1 = new Account();
		Account a2 = new Account();
		
		testT t1 = new testT(a1, a2);
		testT t2 = new testT(a2, a1);
		
		t1.start();
		t2.start();
	}
    
}
