package hw2;

public class CyclicBarrier {
	int value;
	
	public CyclicBarrier(int parties){
		value = parties;
	}
	
	public synchronized void P(){
		value--;
		if (value < 0) 
			Util.myWait(this);
	}
	
	public synchronized void V(){
		value++;
		if (value <= 0)
			notify();
	}
	
	int await() throws InterruptedException{
		
		return -1;
	}

}
