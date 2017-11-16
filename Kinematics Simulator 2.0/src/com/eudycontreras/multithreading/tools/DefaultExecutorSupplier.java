package com.eudycontreras.multithreading.tools;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eudy on 10/14/2016.
 */
public class DefaultExecutorSupplier {

	public static final int THREAD_PRIORITY_BACKGROUND = 10;
	/*
	 * Number of cores to decide the number of threads
	 */
	public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	/*
	 * thread pool executor for background tasks
	 */
	private final ThreadPoolExecutor backgroundExecutor;
	/*
	 * thread pool executor for background prioritized tasks
	 */
	private final PriorityThreadPoolExecutor priorityBackgroundTask;
	/*
	 * thread pool executor for light weight background tasks
	 */
	private final ThreadPoolExecutor lightBackgroundExecutor;
	/*
  	* thread pool executor for main thread tasks
  	*/
	private final Executor mMainThreadExecutor;
	/*
	 * an instance of DefaultExecutorSupplier
	 */
	private static DefaultExecutorSupplier ourInstance;

	public static DefaultExecutorSupplier getInstance() {
		if (ourInstance == null) {
			synchronized (DefaultExecutorSupplier.class) {
				ourInstance = new DefaultExecutorSupplier();
			}
		}
		return ourInstance;
	}

	private DefaultExecutorSupplier() {

		// setting the thread factory
		ThreadFactory backgroundPriorityThreadFactory = new PriorityThreadFactory(THREAD_PRIORITY_BACKGROUND);

		// setting the thread pool executor for Background Tasks;
		backgroundExecutor = new ThreadPoolExecutor(
				NUMBER_OF_CORES * 2,
				NUMBER_OF_CORES * 2,
				60L,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				backgroundPriorityThreadFactory
				);

		// setting the thread pool executor for Light Weight BackgroundTasks;
		lightBackgroundExecutor = new ThreadPoolExecutor(
				NUMBER_OF_CORES * 2,
				NUMBER_OF_CORES * 2,
				60L,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				backgroundPriorityThreadFactory
				);
		// setting the thread pool executor for priorityBackgroundTask;
		priorityBackgroundTask = new PriorityThreadPoolExecutor(
				NUMBER_OF_CORES * 2,
				NUMBER_OF_CORES * 2,
				60L,
				TimeUnit.SECONDS,
				backgroundPriorityThreadFactory
				);

		mMainThreadExecutor = new MainThreadExecutor();
	}

	/*
	 * returns the thread pool executor for background task
	 */
	public ThreadPoolExecutor backgroundTasks() {
		return backgroundExecutor;
	}

	/*
	 * returns the thread pool executor for light weight background task
	 */
	public ThreadPoolExecutor lightWeightBackgroundTasks() {
		return lightBackgroundExecutor;
	}

	/*
	 * returns the priority thread pool executor for background task
	 */
	public PriorityThreadPoolExecutor prioritizeBackgroundTask() {
		return priorityBackgroundTask;
	}

	/*
    * returns the thread pool executor for main thread task
    */
	public Executor forMainThreadTasks() {
		return mMainThreadExecutor;
	}

}

