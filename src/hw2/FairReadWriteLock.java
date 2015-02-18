package hw2;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FairReadWriteLock{
	
	boolean writing;
	int waitingWriters;
	int waitingReaders;
	int readers;
	ConcurrentLinkedQueue<String> queue;
	ReadWriteLockLogger logger;
	
	public FairReadWriteLock(){
		this.writing = false;
		this.readers = 0;
		this.queue = new ConcurrentLinkedQueue<String>();

		this.logger = new ReadWriteLockLogger();
	}
	
	public synchronized void beginRead(){
		//logger.logTryToRead();

		String add = "R-" + Thread.currentThread().getId();
		queue.add(add);
		
		try {
			while(writing || isPrecedingWriter(add)) //writing or writers ahead
				wait();
		} catch (InterruptedException e) {e.printStackTrace();}

		readers += 1;
		queue.remove(add);
		
		//logger.logBeginRead();
	}
	
	private boolean isPrecedingWriter(String s) {
		for(String checker: queue){
			if(checker.equals(s)){
				break;
			}
			else if(checker.contains("W-")){
					return true;
			}
		}
		
		return false;
	}

	public synchronized void endRead(){	
		readers -= 1;
		
		if(readers == 0)
			notifyAll();
	
		//logger.logEndRead();
	}
	
	public synchronized void beginWrite(){
		//logger.logTryToWrite();

		String add = "W-" + Thread.currentThread().getId();
		queue.add(add);
		
		try{
			while( (readers > 0) || writing || !queue.peek().equals(add) )
				wait();
		} catch(InterruptedException e){ e.printStackTrace();}

		queue.remove(add);
		writing = true;
		
		//logger.logBeginWrite();
	}
	
	public synchronized void endWrite(){
		writing = false;
		notifyAll();

		//logger.logEndWrite();
	}
}
