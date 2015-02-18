package hw2;

import java.util.Collections;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
	public static void main(String[] args){
		CyclicMain(5);
		//Test the FairReadWriteLock class
		//FairMain();
		
		//Test the Garden class
//		GardenMain();	
	}
	
	public static void CyclicMain(int N){
		MyThread t[];
		
		CyclicBarrier Cyc = new CyclicBarrier(N);
        t = new MyThread[N];
        for (int j=0; j < N; j++){
        for (int i = 0; i < N; i++) {
     	
        	try{
       		Thread.sleep(500);
        	}catch (InterruptedException e){
        		e.printStackTrace();
        	}
        	
     	
            t[i] = new MyThread(i, Cyc);
            t[i].start();
        }
        }
	
	private static void FairMain()
	{
		FairReadWriteLock frwl = new FairReadWriteLock();
		
		Writer w1 = new Writer(frwl, 3);
		Reader r1 = new Reader(frwl, 3);
		Reader r2 = new Reader(frwl, 3);
		
		Thread write1 = new Thread(w1);
		Thread read1 = new Thread(r1);
		Thread read2 = new Thread(r2);
		
		read2.start();
		read1.start();
		write1.start();
		
		try{
            write1.join();
            read1.join();
            read2.join();
         } catch(Exception ex){
            System.out.println("Exception while waiting for join: " + ex.getMessage());
         }
	}
	
	
	private static void GardenMain() {
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		GardenLogger gLog = new GardenLogger();
		Garden garden = new Garden(2, gLog);
	
		Newton newt = new Newton(garden, gLog, 3);
		Benjamin ben = new Benjamin(garden, gLog);
		Mary mary = new Mary(garden, gLog);
		
		/*
		Thread Newton = new Thread(newt);
		Thread Benjamin = new Thread(ben);
		Thread Mary = new Thread(mary);
		
		Mary.start();
		Benjamin.start();
		Newton.start();
		*/
		
		Future<?> f1 = threadPool.submit(newt);
		Future<?> f2 = threadPool.submit(ben);
		Future<?> f3 = threadPool.submit(mary);
		
		try{
			f1.get();
			f2.get();
			f3.get();
		} catch(Exception e){e.printStackTrace();}
		finally{
			threadPool.shutdown();
		}
	}
}


class Writer implements Runnable{
	FairReadWriteLock lock;
	int iterations;
	
	public Writer(FairReadWriteLock l, int i){
		this.lock = l;
		this.iterations = i;
	}
	
	public void run(){
		while(iterations > 0){
			lock.beginWrite();
			try {
				Thread.sleep(6000); //wait time 6sec
			} catch (InterruptedException e) {e.printStackTrace();} 
			lock.endWrite();
			
			iterations -= 1;
		}
	}
}


class Reader implements Runnable{
	FairReadWriteLock lock;
	int iterations;
	
	public Reader(FairReadWriteLock l, int i){
		this.lock = l;
		this.iterations = i;
	}
	
	public void run(){
		while(iterations > 0){
			lock.beginRead();
			try {
				Thread.sleep(6000); //wait time 6sec
			} catch (InterruptedException e) {e.printStackTrace();} 
			lock.endRead();
			
			iterations -= 1;
		}
	}
}


class Newton extends Thread{
	Garden garden;
	int iterations;
	
	public Newton(Garden g, GardenLogger l, int i){
		this.garden = g;
		this.iterations = i;
	}
	
	public void dig(){
		garden.unfilled += 1;
	}
	
	public void run(){
		while(iterations > 0){
			try{
				garden.startDigging();
			} catch(InterruptedException e) {}
			dig();
			
			try {
				Thread.sleep(3000); //wait time 3sec
			} catch (InterruptedException e) {e.printStackTrace();} 
			garden.doneDigging();
			
			iterations -= 1;
		}
	}
}

class Benjamin extends Thread{
	Garden garden;
	
	public Benjamin(Garden g, GardenLogger l){
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
			
			try {
				Thread.sleep(3000); //wait time 3sec
			} catch (InterruptedException e) {e.printStackTrace();} 
			
			garden.doneSeeding();
		}
	}
}

class Mary extends Thread{
	Garden garden;
	
	public Mary(Garden g, GardenLogger l){
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
			
			try {
				Thread.sleep(3000); //wait time 3sec
			} catch (InterruptedException e) {e.printStackTrace();} 
			
			garden.doneFilling();
		}
	}
}
