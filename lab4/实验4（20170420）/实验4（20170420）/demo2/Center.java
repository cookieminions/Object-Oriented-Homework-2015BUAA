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
       
    }  
       //取号机产生新号码
    public void produce() {  
       
        
    }  
  //客户获得服务，请注意线程安全的实现
    public void consume() {  
        
    }  
} 