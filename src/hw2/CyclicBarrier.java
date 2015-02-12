package hw2;

public class CyclicBarrier {
	int value;
	
	public CyclicBarrier(int parties){
		value = parties;
	}
/*	
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
*/
// Random Comment	
	int await() throws InterruptedException{
		value--;
		if (value == 0){
			notifyAll();
		}
		else
			this.wait();
		
		return value;
	}

}
