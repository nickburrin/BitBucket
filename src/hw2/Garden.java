package hw2;

import java.util.concurrent.locks.*;

public class Garden {

	GardenLogger log;
	static int MAX;
	int unfilled;
	int planted;

	final ReentrantLock shovel = new ReentrantLock();
	final ReentrantLock seedLock = new ReentrantLock();
	final Condition maxUnfilled = shovel.newCondition();
	final Condition waitingToPlant = seedLock.newCondition();
	final Condition waitingToFill = shovel.newCondition();

	public Garden(int max, GardenLogger gLog){
		this.MAX = max;
		this.log = gLog;
		this.unfilled = 0;
		this.planted = 0;
	}

	/**
	 * Newton digs the holes. He will only dig a new hole if: unfilled <= MAX.
	 * He must first grab the shovel.
	 */
	public void startDigging() throws InterruptedException{
		log.logTryToDig();
		
		shovel.lock();
		
		try{
			while(unfilled >= MAX){
				maxUnfilled.await();
			}	
		}
		finally{
			shovel.unlock();
		}
	}

	/**
	 * Newton is done digging. Puts back the shovel and wakes any threads waiting to plant
	 */
	public void doneDigging(){
		seedLock.lock();
		waitingToPlant.signal();
		seedLock.unlock();
		
		log.logDoneDigging(unfilled);
		
		//shovel.unlock();
	}

	/**
	 * Benjamin will plant a seed if: unfilled > 0
	 */
	public void startSeeding() throws InterruptedException{
		log.logTryToPlant();
		
		seedLock.lock();
		
		try{
			while(unfilled < 1){
				waitingToPlant.await();
			}
		}
		finally{
			seedLock.unlock();
		}
	}

	public void doneSeeding(){
		shovel.lock();
		waitingToFill.signal();
		shovel.unlock();
		
		log.logDonePlanting(planted);
		
		//seedLock.unlock();
	}

	/**
	 * Mary will fill a hole as long as it has been "planted": planted > 0 
	 */
	public void startFilling() throws InterruptedException{
		log.logTryToFill();
		
		shovel.lock();
		
		try{
			while(planted < 1){
				waitingToFill.await();
			}
		}
		finally{
			shovel.unlock();
		}
	}

	/**
	 * Mary needs to release the shovel and notify Newton if: filled < MAX
	 */
	public void doneFilling(){
		shovel.lock();
		
		try{
			if(unfilled < MAX){
				maxUnfilled.signal();
			}
		}
		finally{
			log.logDoneFilling(unfilled, planted);
			shovel.unlock();
		}
	}
}
