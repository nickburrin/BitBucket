package hw2;

import java.util.concurrent.locks.*;

public class Garden {

	static int MAX;
	int unfilled;
	int planted;

	final ReentrantLock shovel = new ReentrantLock();
	final ReentrantLock seedLock = new ReentrantLock();
	final Condition maxUnfilled = shovel.newCondition();
	final Condition waitingToPlant = seedLock.newCondition();
	final Condition waitingToFill = shovel.newCondition();

	public Garden(int max){
		this.MAX = max;
		this.unfilled = 0;
		this.planted = 0;
	}

	/**
	 * Newton digs the holes. He will only dig a new hole if: unfilled <= MAX.
	 * He must first grab the shovel.
	 */
	public void startDigging() throws InterruptedException{
		shovel.lock();
	
		while(unfilled >= MAX){
			maxUnfilled.await();
			System.out.println("The max has been reached. Newton is waiting.");
		}	
	}

	/**
	 * Newton is done digging. Puts back the shovel and wakes any threads waiting to plant
	 */
	public void doneDigging(){
		seedLock.lock();
		waitingToPlant.signal();
		seedLock.unlock();
		
		shovel.unlock();
	}

	/**
	 * Benjamin will plant a seed if: unfilled > 0
	 */
	public void startSeeding() throws InterruptedException{
		seedLock.lock();
		
		while(unfilled < 1){
			waitingToPlant.await();
			System.out.println("There are no holes to fill. Benjamin is waiting.");
		}
	}

	public void doneSeeding(){
		shovel.lock();
		waitingToFill.signal();
		shovel.unlock();
		
		seedLock.unlock();
	}

	/**
	 * Mary will fill a hole as long as it has been "planted": planted > 0 
	 */
	public void startFilling() throws InterruptedException{
		shovel.lock();

		while(planted < 1){
			waitingToFill.await();
			System.out.println("There are no planted holes. Mary is waiting.");
		}
	}

	/**
	 * Mary needs to release the shovel and notify Newton if: filled < MAX
	 */
	public void doneFilling(){
		if(unfilled < MAX){
			maxUnfilled.signal();
		}
		shovel.unlock();
	}
}
