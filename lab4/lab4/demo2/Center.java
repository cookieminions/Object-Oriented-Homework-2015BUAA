package demo2;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 服务中心
 
 */
public class Center extends Thread {  
    private BlockingQueue<Waiter> waiters;  
    private BlockingQueue<Customer> customers;  
  
   
    private final static int PRODUCERSLEEPSEED = 3000;  
    private final static int CONSUMERSLEEPSEED = 100000;  
  
    public Center(int num) {  
    	//创建提供服务的柜台队列和取得号的客户队列
    	waiters = new LinkedBlockingQueue<Waiter>(num);
    	for(int i=0;i<num;i++){
    		Waiter waiter = new Waiter();
    		try {
				waiters.put(waiter);
			} catch (InterruptedException e) {}
    	}
    	customers = new LinkedBlockingQueue<Customer>();
    	
    }  
       //取号机产生新号码
    public void produce() {  
    	Customer newCustomer = new Customer();
        System.out.println(newCustomer.toString()+"号顾客正在等待服务");
        try {
        	customers.put(newCustomer);
			sleep(PRODUCERSLEEPSEED/10);
		} catch (InterruptedException e1) {
		}
       
    }  
  //客户获得服务，请注意线程安全的实现
    public void consume() {  
        try {
			Waiter takeWaiter = waiters.take();
			Customer newCustomer = customers.take();
			System.out.println("请"+newCustomer.toString()+"号顾客到"+takeWaiter.toString()+"号窗口");
			System.out.println(takeWaiter.toString()+"号窗口正在为"+newCustomer.toString()+"号顾客办理业务");
			sleep(CONSUMERSLEEPSEED/10);
			waiters.put(takeWaiter);
		} catch (InterruptedException e) {}
        
    }  
} 