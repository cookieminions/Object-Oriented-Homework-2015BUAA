package demo1;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import exp41.Constants;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class CalNum {

/** 测试程序入口**/
	 
	    public static void main(String[] args){
	    	
	    	//生产者消费者线程锁
	        Object producerMonitor = new Object();
	        Object consumerMonitor = new Object();
	        
	        //设定一个足够大的容器
	        Container<Customer> container = new Container<Customer>(10000);
	        
	        //生产者线程启动
	        new Thread(new Producer(producerMonitor,consumerMonitor,container)).start();
	        //消费者线程启动，模拟10个柜台
	        for(int i=1;i<=10;i++){
	         { new Thread(new Consumer(producerMonitor,consumerMonitor,container,i)).start();
	         }
	 	  }
	   }  
     }
	
