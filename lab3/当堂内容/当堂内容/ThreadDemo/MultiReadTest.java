package ThreadDemo;

import java.io.File;  
import java.io.RandomAccessFile;  
import java.util.concurrent.CountDownLatch;  
  
public class MultiReadTest {  
  
    /** 
     * 多线程读取文件测试 
     * @param args 
     */  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        final int DOWN_THREAD_NUM = 10;//起10个线程去读取指定文件  
        final String OUT_FILE_NAME = "e:\\1.txt";  
        final String keywords = "无忌";  
         //jdk1.5以上版本提供的线程辅助类，让主线程等待所有子线程执行完毕后使用的类，  
        CountDownLatch doneSignal = new CountDownLatch(DOWN_THREAD_NUM);  
        RandomAccessFile[] outArr = new RandomAccessFile[DOWN_THREAD_NUM];  
        try{  
            long length = new File(OUT_FILE_NAME).length();  
            System.out.println("文件总长度："+length+"字节");  
            //每线程应该读取的字节数    
            long numPerThred = length / DOWN_THREAD_NUM;    
            System.out.println("每个线程读取的字节数："+numPerThred+"字节");  
          //整个文件整除后剩下的余数    
            long left = length % DOWN_THREAD_NUM;  
            for (int i = 0; i < DOWN_THREAD_NUM; i++) {    
                //为每个线程打开一个输入流、一个RandomAccessFile对
                outArr[i] = new RandomAccessFile(OUT_FILE_NAME, "rw");    
                if (i == DOWN_THREAD_NUM - 1) {    
                      new ReadThread(i * numPerThred, (i + 1) * numPerThred    
                            + left, outArr[i],keywords,doneSignal).start();    
                } else {    
                    //每个线程负责读取一定的numPerThred个字节    
                	new ReadThread(i * numPerThred, (i + 1) * numPerThred,    
                            outArr[i],keywords,doneSignal).start();    
                }    
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
 
        //确认所有线程任务完成，开始执行主线程的操作  
        try {  
            doneSignal.await();  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        //这里需要做个判断，所有做read工作线程全部执行完。  
        KeyWordsCount k = KeyWordsCount.getCountObject();  
        System.out.println("指定关键字出现的次数："+k.getCount());  
    }  
  
}  