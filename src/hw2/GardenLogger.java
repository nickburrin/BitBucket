package hw2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GardenLogger {
	
	private AtomicInteger reqNo = new AtomicInteger();
	private HashMap<Thread, Integer> mapThreadReqNo = new HashMap<Thread, Integer>();
	
	public void logTryToDig() {
		System.out.println("[" + reqNo + "] " + "Thread " + Thread.currentThread().getId() + " requests to dig");
		mapThreadReqNo.put(Thread.currentThread(), reqNo.getAndIncrement());
	}
	
	public void logDoneDigging(int unfilled){
		int myReqNo = mapThreadReqNo.get(Thread.currentThread());
		System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " is done digging. " + unfilled + " unfilled holes.");
	}
	
	public void logTryToPlant() {
		System.out.println("[" + reqNo + "] " + "Thread " + Thread.currentThread().getId() + " requests to plant");
		mapThreadReqNo.put(Thread.currentThread(), reqNo.getAndIncrement());
	}

	public void logDonePlanting(int planted){
		int myReqNo = mapThreadReqNo.get(Thread.currentThread());
		System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " is done planting. " + planted + " planted holes");
	}
	
	public void logTryToFill() {
		System.out.println("[" + reqNo + "] " + "Thread " + Thread.currentThread().getId() + " requests to fill");
		mapThreadReqNo.put(Thread.currentThread(), reqNo.getAndIncrement());
	}

	public void logDoneFilling(int unfilled, int planted){
		int myReqNo = mapThreadReqNo.get(Thread.currentThread());
		System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " is done filling. " + unfilled + " unfilled and " + planted + " planted");
	}

	
}

