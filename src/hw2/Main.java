package hw2;

public class Main {
	public static void main(String[] args){
		
		//Test the FairReadWriteLock class
		FairMain();
		//Test the Garden class
		//GardenMain();	
	}
	
	private static void FairMain()
	{
		FairReadWriteLock frwl = new FairReadWriteLock();
		Writer w = new Writer(frwl);
		Reader r = new Reader(frwl);
		
		Thread write = new Thread(w);
		Thread read = new Thread(r);
		
		write.start();
		read.start();
	}
	
	
	private static void GardenMain() {
		
		Garden garden = new Garden(4);
	
		Newton newt = new Newton(garden);
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
	
	public Writer(FairReadWriteLock l){
		this.lock = l;
	}
	
	public void run(){
		while(true){
			lock.beginWrite();
			lock.endWrite();
		}
	}
}


class Reader extends Thread{
	FairReadWriteLock lock;
	
	public Reader(FairReadWriteLock l){
		this.lock = l;
	}
	
	public void run(){
		while(true){
			lock.beginRead();
			lock.endRead();
		}
	}
}


class Newton extends Thread{
	Garden garden;
	
	public Newton(Garden g){
		this.garden = g;
	}
	
	public void dig(){
		garden.unfilled += 1;
	}
	
	public void run(){
		while(true){
			try{
				garden.startDigging();
			} catch(InterruptedException e) {}
			dig();
			garden.doneDigging();
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
