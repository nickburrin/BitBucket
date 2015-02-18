package hw2;
import java.util.Random;


public class MyThread extends Thread {
    int myId;
    CyclicBarrier Cyc;

    public MyThread(int id, CyclicBarrier c) {
    	this.myId = id;
        this.Cyc = c; 
    }
    
    public void run() {
        
          //  System.out.println("before");
            try {
 //           	System.out.println("Thread index: " + Cyc.await());
            	System.out.println("Thread " + myId +" Started");
            	Cyc.await();
            	System.out.println("Barrier " + myId + " Unlocked");
            	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Hey, you hit an error!");
			}
            finally{
     //       	System.out.println("after");  
            }
        
    }
    
    
}