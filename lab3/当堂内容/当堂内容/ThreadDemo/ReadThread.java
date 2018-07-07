package ThreadDemo;

import java.io.IOException;  
import java.io.RandomAccessFile;  
import java.util.concurrent.CountDownLatch;  
  
//用来读取文件，当获取到指定关键字时，在指定的对象加1 
 public class ReadThread extends Thread{  
    //定义字节数组的长度    
    private final int BUFF_LEN = 256;    
    //定义读取的起始点    
    private long start;    
    //定义读取的结束点    
    private long end;   
    //将读取到的字节输出到raf中,randomAccessFile类的用法请自行百度  
    private RandomAccessFile raf;    
    //线程中需要指定的关键字  
    private String keywords;  
    //此线程读到关键字的次数  
    private int curCount = 0;  
    /** 
     * jdk1.5开始加入的类，是个多线程辅助类 
     * 用于多线程开始前统一执行操作或者多线程执行完成后调用主线程执行相应操作的类 
     */  
    private CountDownLatch doneSignal;  
    public ReadThread(long start, long end, RandomAccessFile raf,String keywords,CountDownLatch doneSignal){  
        this.start = start;  
        this.end = end;  
        this.raf  = raf;  
        this.keywords = keywords;  
        this.doneSignal = doneSignal;  
    }  
      
    public void run(){  
        try {  
            raf.seek(start);  
            //本线程负责读取文件的大小    
            long contentLen = end - start;    
            //定义最多需要读取几次就可以完成本线程的读取    
            long times = contentLen / BUFF_LEN+1;    
            System.out.println(this.toString() + " 需要读的次数："+times);  
            byte[] buff = new byte[BUFF_LEN];  
            int hasRead = 0;  
            String result = null;  
            for (int i = 0; i < times; i++) {    
                //之前SEEK指定了起始位置，这里读入指定字节组长度的内容，read方法返回的是下一个开始读的position  
                hasRead = raf.read(buff);  
                 //如果读取的字节数小于0，则退出循环！ （到了字节数组的末尾）   
                if (hasRead < 0) {    
                    break;    
                }    
                result = new String(buff,"gb2312");  
                int count = this.getCountByKeywords(result, keywords);  
                if(count > 0){  
                    this.curCount += count;  
                }  
            }  
              
            KeyWordsCount kc = KeyWordsCount.getCountObject();  
  
            kc.addCount(this.curCount);  
              
            doneSignal.countDown();//current thread finished! noted by latch object!  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }
  
       
    public int getCountByKeywords(String statement,String key){  
        return statement.split(key).length-1;  
    }  
  
   
}  