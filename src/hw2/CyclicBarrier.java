package hw2;

import java.util.concurrent.Semaphore;

public class CyclicBarrier{
	static int threads;
	static int initValue;
	Semaphore BinarySem;
	Semaphore CountingSem;
	public CyclicBarrier(int parties){
		BinarySem = new Semaphore(1);
		CountingSem = new Semaphore(0);
		threads = parties;
		initValue = parties;
	}

//just need to lock value, initValue, and the acquire and release
	int await() throws InterruptedException{
		
		try{
			//try to acquire the lock so threads are not decremented at the same time
			BinarySem.acquire();
			
			//decrement number of threads
			threads--;
			BinarySem.release();
			if (threads > 0){
				
				CountingSem.acquire();
				
			}
			else{
				//do we need to reset the threads?
				BinarySem.acquire();
				threads = initValue;
				BinarySem.release();
				CountingSem.release(initValue - 1);
//				CountingSem.acquire();//if you reuse this, you have to keep this line
//				System.out.println("before the acquire, there were " + CountingSem.availablePermits() + " remaining");
//				BinarySem.release();
			}
		}catch (InterruptedException e){
			System.out.println("bonesaw");

		}
		finally{
			
		}
		
		return threads;
		
		
	}
}


