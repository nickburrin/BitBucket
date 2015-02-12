package hw2;

import java.util.Collections;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
	public static void main(String[] args){

		//Test the FairReadWriteLock class
		//FairMain();
		
		//Test the Garden class
		GardenMain();	
	}
	
	private static void FairMain()
	{
		ExecutorService threadPool = Executors.newCachedThreadPool();
		long begin = System.currentTimeMillis();
		FairReadWriteLock frwl = new FairReadWriteLock();
		
		
		Writer w = new Writer(frwl, 1, begin);
		Reader r = new Reader(frwl, 0, begin);
		
		Thread write = new Thread(w);
		Thread read = new Thread(r);
		
		write.start();
		read.start();
		
		
		System.out.println("Reader" + r.timeStamps);
		System.out.println("Writer" + w.timeStamps);
	}
	
	
	private static void GardenMain() {
		
		Garden garden = new Garden(4);
	
		Newton newt = new Newton(garden, 10);
		Benjamin ben = new Benjamin(garden);
		Mary mary = new Mary(garden);
		
		Thread Newton = new Thread(newt);
		Thread Benjamin = new Thread(ben);
		Thread Mary = new Thread(mary);
		
		Newton.start();
		Benjamin.start();
		Mary.start();
	}
}


class Writer extends Thread{
	FairReadWriteLock lock;
	TreeMap<Long, String> timeStamps = new TreeMap<Long, String>();
	int iterations;
	long begin;
	
	public Writer(FairReadWriteLock l, int i, long b){
		this.lock = l;
		this.iterations = i;
		this.begin = b;
	}
	
	public void run(){
		while(iterations > 0){
			timeStamps.put(System.currentTimeMillis() - begin, "Writer in");
			lock.beginWrite();
			lock.endWrite();
			timeStamps.put(System.currentTimeMillis() - begin, "Writer out");
			
			iterations -= 1;
		}
	}
}


class Reader extends Thread{
	FairReadWriteLock lock;
	TreeMap<Long, String> timeStamps = new TreeMap<Long, String>();
	int iterations;
	long begin;
	
	public Reader(FairReadWriteLock l, int i, long b){
		this.lock = l;
		this.iterations = i;
		this.begin = b;
	}
	
	public void run(){
		while(iterations > 0){
			timeStamps.put(System.currentTimeMillis() - begin, "Reader in");
			lock.beginRead();
			lock.endRead();
			timeStamps.put(System.currentTimeMillis() - begin, "Reader out");
			
			iterations -= 1;
		}
	}
}


class Newton extends Thread{
	Garden garden;
	int MAX;
	int holes;
	
	public Newton(Garden g, int m){
		this.garden = g;
		this.MAX = m;
	}
	
	public void dig(){
		garden.unfilled += 1;
	}
	
	public void run(){
		while(holes < MAX){
			try{
				garden.startDigging();
			} catch(InterruptedException e) {}
			dig();
			garden.doneDigging();
			holes += 1;
		}
	}
}

class Benjamin extends Thread{
	Garden garden;
	
	public Benjamin(Garden g){
		this.garden = g;
	}
	
	public void plantSeed(){
		garden.planted += 1;
	}
	
	public void run(){
		while(true){
			try{
				garden.startSeeding();
			} catch(InterruptedException e){}
			plantSeed();
			garden.doneSeeding();
		}
	}
}

class Mary extends Thread{
	Garden garden;
	
	public Mary(Garden g){
		this.garden = g;
	}
	
	public void Fill(){
		garden.planted -= 1;
		garden.unfilled -= 1;		
	}
	
	public void run(){
		while(true){
			try{
				garden.startFilling();
			} catch(InterruptedException e) {}
			Fill();
			garden.doneFilling();
		}
	}
}
