package com.eudycontreras.multithreading.tools;

public class EventQueue {

	private ThreadBuffer<Runnable> buffer = new ThreadBuffer<>();

	private WorkerThread worker;

	public void start() {
		if(worker==null) {
		    worker = new WorkerThread();
		    worker.start();
		}
	}

	public void stop() {
		if(worker!=null) {
		    worker.interrupt();
		    worker=null;
		}
	}

	public final boolean isRunning() {
		return worker.isAlive();
	}

	public EventQueue execute(Runnable runnable){
		buffer.add(runnable);
		return this;
	}

	private class WorkerThread extends Thread {
		public void run() {
			Runnable runnable;
			while(worker!=null) {
				try {
					runnable = buffer.get();
					runnable.run();
				} catch (InterruptedException e) {
					worker=null;
				}
			}
		}
	}
}
