package demo1;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 消费者
*/
public class Consumer implements Runnable{
	 private int windowId;
    
    //模拟生产容器，设置成final类型的话不允许再次赋值
    private final Container<Customer> container;
    //生产者线程监听器
    private final Object producerMonitor;
    //消费者线程监听器
    private final Object consumerMonitor;
    
    
    private final static int CONSUMERSLEEPSEED = 100000;  
    
    public Consumer(Object producerMonitor,Object consumerMonitor,Container<Customer> container,int id){
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
        this.windowId=id;
    }

    @Override
    public void run() {
        while(true){
            consume();
        }
    }
    
    //消费
    public void consume(){
        
        }       
    }
