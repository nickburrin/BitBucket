package hw2;

import java.util.concurrent.locks.*;

public class Garden {
	
	static int MAX;
	static int unfilled;
	static int planted;
	
	final ReentrantLock shovel = new ReentrantLock();
	final Condition maxUnfilled = shovel.newCondition();
	final Condition waitingToPlant = shovel.newCondition();
	final Condition waitingToFill = shovel.newCondition();
	
	public Garden(int max){
		this.MAX = max;
		this.unfilled = 0;
		this.planted = 0;
	}
	
	/**
	 * Newton digs the holes. He will only dig a new hole if: unfilled <= MAX.
	 * He must first grab the shovel.
	 * ???What happens if Newton cannot dig a hole and Mary wants the shovel? When is shovel unlocked???
	 */
	public void startDigging() throws InterruptedException{
		
		shovel.lock();
		
		try{
			while(unfilled >= MAX){
				maxUnfilled.await();
			}	
			//unfilled += 1;
			
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
	 * Benjamin will plant a seed if: unfilled > 0
	 */
	public void startSeeding(){
		
		try{
			while(unfilled < 1){
				waitingToPlant.await();	
			}
			
			//planted += 1;
		} catch(InterruptedException e){}
		
	}
	
	public void doneSeeding(){
		waitingToFill.signal();
	}
	
	/**
	 * Mary will fill a hole as long as it has been "planted": planted > 0 
	 */
	public void startFilling(){
		shovel.lock();
		
		try{
			while(planted < 1){
				waitingToFill.await();
			}	
			//planted -= 1;
			//unfilled -= 1;
			
		} catch(InterruptedException e) {}
	}
	
	/**
	 * Mary needs to release the shovel and notify Newton if: filled < MAX
	 */
	public void doneFilling(){
		shovel.unlock();
		
		if(unfilled < MAX){
			maxUnfilled.signal();
		}
	}
}
