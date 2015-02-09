package hw2;

import java.util.concurrent.locks.ReentrantLock;

public class Garden {
	static int MAX;
	ReentrantLock shovel = new ReentrantLock();
	
	public Garden(int max){
		this.MAX = max;
	}
	
	public void startDigging(){
		System.out.println("This is a test");
	}
	
	public void doneDigging(){
		
	}
	
	public void startSeeding(){
		
	}
	
	public void doneSeeding(){
		
	}
	
	public void startFilling(){
		
	}
	
	public void doneFilling(){
		
	}
}
