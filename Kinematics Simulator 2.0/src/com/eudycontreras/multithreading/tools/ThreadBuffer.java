package com.eudycontreras.multithreading.tools;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadBuffer<T> {

	private Queue<T> buffer = new LinkedList<>();
	private int size;

	public ThreadBuffer(){
		this(Integer.MAX_VALUE);
	}

	public ThreadBuffer(int size) {
		this.size = size;
	}

	public synchronized void add(T element) {
		buffer.add(element);

		if(size()>size){
			buffer.poll();
		}
		notifyAll();
	}

	public synchronized T get() throws InterruptedException {
		while(buffer.isEmpty()) {
			wait();
		}
		return buffer.poll();
	}

	public int size() {
		return buffer.size();
	}
}
