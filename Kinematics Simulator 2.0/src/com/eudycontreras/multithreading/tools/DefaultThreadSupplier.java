package com.eudycontreras.multithreading.tools;

public class DefaultThreadSupplier{


	private final EventQueue normalTask;

	private static DefaultThreadSupplier ourInstance;

	public static DefaultThreadSupplier getInstance() {
		if (ourInstance == null) {
			synchronized (DefaultThreadSupplier.class) {
				ourInstance = new DefaultThreadSupplier();
			}
		}
		return ourInstance;
	}

	private DefaultThreadSupplier() {
		normalTask = new EventQueue();
	}

	public final EventQueue forNormalTask() {
		return normalTask;
	}




}
