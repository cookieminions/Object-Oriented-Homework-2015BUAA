package lab3;

import java.util.Random;

public class SaleTicket implements Runnable {

	public int total;
	public int count;

	public SaleTicket() {
		total = 100;
		count = 0;
	}
	
	public void run() {
		while (total > 0) {
			synchronized (this) {
				if(total > 0) {
					try {
						this.wait(new Random().nextInt(1000));
					} catch (InterruptedException e) {	
						e.printStackTrace();
					}
					if(total>0){
						count++;
						total--;
						System.out.println(Thread.currentThread().getName() + "\t当前票号：" + count);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		SaleTicket st = new SaleTicket();
		for(int i=1; i<=5; i++) {
			new Thread(st, "售票点" + i).start();
		}
	}
}

