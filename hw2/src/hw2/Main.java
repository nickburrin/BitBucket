package hw2;

import hw2.CyclicBarrier;
import hw2.MyThread;

public class Main{
	public static void main(String args[]){
		
		CyclicMain(5);
/*
		CyclicMain(10);
		CyclicMain(20);
		CyclicMain(40);
		CyclicMain(100);
*/
	}
	
	public static void CyclicMain(int N){
		MyThread t[];
		
		CyclicBarrier Cyc = new CyclicBarrier(N);
        t = new MyThread[N];
        for (int j=0; j < N; j++){
	        for (int i = 0; i < N; i++) {
	     	
	        	try{
	       		Thread.sleep(500);
	        	}catch (InterruptedException e){
	        		e.printStackTrace();
	        	}
	        	
	     	
	            t[i] = new MyThread(i, Cyc);
	            t[i].start();
	        }
        }
  /*     
        for (int i = 0; i < (N-1); i++) {
        	try{
        		Thread.sleep(500);
        	}catch (InterruptedException e){
        		e.printStackTrace();
        	}
        	
            t[i] = new MyThread(i, Cyc);
            t[i].start();
        }
 */       
	}
	
	//CyclicBarrier.await();
}
