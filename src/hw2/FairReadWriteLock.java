package hw2;

public class FairReadWriteLock{
	
	boolean writing = false;
	int waitingWriters = 0;
	int readers = 0;
	
	synchronized void beginRead(){
		
		try {
			while(writing || waitingWriters > 0)
				wait();
		} catch (InterruptedException e) {}
		
		readers += 1;
	}
	
	synchronized void endRead(){
		readers -= 1;
		notifyAll();
	}
	
	synchronized void beginWrite(){
		
		waitingWriters += 1;
		
		try{
			while((readers > 0) || (writing = true))
				wait();
		} catch(InterruptedException e){}
		
		writing = true;
		waitingWriters -= 1;
		//lock the resource
	}
	
	synchronized void endWrite(){
		writing = false;
		notifyAll();
	}
}
