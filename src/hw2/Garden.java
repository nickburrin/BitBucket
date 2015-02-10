package hw2;

import java.util.concurrent.locks.*;

public class Garden {
	
	static int MAX;
	static int empty;
	static int planted;
	static int filled;
	
	final ReentrantLock shovel = new ReentrantLock();
	final Condition maxEmpty = shovel.newCondition();
	final Condition noneEmpty = shovel.newCondition();
	final Condition waitingToPlant = shovel.newCondition();
	
	public Garden(int max){
		this.MAX = max;
		this.empty = 0;
		this.planted = 0; 
		this.filled = 0; 
	}
	
	/**
	 * Newton digs the holes. He will only dig a new hole if: empty <= MAX.
	 * He must first grab the shovel.
	 */
	public void startDigging() throws InterruptedException{
		
		shovel.lock();
		
		try{
			while(empty >= MAX){
				maxEmpty.await();
			}	
			//empty += 1;
			
		} catch(InterruptedException e) {}
	}
	
	/**
	 * Newton is done digging. Puts back the shovel and wakes any threads waiting to plant
	 */
	public void doneDigging(){
		shovel.unlock();
		waitingToPlant.signal();
	}
	
	/**
	 * Benjamin will plant a seed if: empty > 0
	 */
	public void startSeeding(){
		
		try{
			while(empty < 1){
				waitingToPlant.await();	
			}
			
			//empty -= 1;
			//planted += 1;
		} catch(InterruptedException e){}
		
	}
	
	public void doneSeeding(){
		
	}
	
	public void startFilling(){
		
	}
	
	public void doneFilling(){
		
	}
}
