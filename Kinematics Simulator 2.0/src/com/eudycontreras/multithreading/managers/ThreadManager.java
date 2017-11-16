package com.eudycontreras.multithreading.managers;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.eudycontreras.multithreading.timers.Period;
import com.eudycontreras.multithreading.timers.TimePeriod;
import com.eudycontreras.multithreading.tools.EventQueue;
import com.eudycontreras.multithreading.tools.TaskWrapper;
import com.eudycontreras.multithreading.tools.ValueTask;
import com.eudycontreras.multithreading.tools.WorkerThread;


/**
 * Thread managing class which allows the managing of threads
 * create for different task. Each time a task is submitted to this
 * class a thread is created for the handling of said task.
 * Created by Eudy Contreras on 10/14/2016.
 */

public class ThreadManager {

    private ConcurrentHashMap<String, WorkerThread> threads;
    private EventQueue eventQueue;


    /**
     * Constructor which initializes this manager
     */
    public ThreadManager(){
       threads = new ConcurrentHashMap<>();
    }

    public synchronized ThreadManager sumbitThread(String name, TaskWrapper... tasks){
    	return submitThread(name, WorkerThread.TaskType.CONTINUOUS,tasks);
	}

    public synchronized ThreadManager submitThread(String name, WorkerThread.TaskType taskType, TaskWrapper... tasks){
    	return submitThread(name,taskType, TimePeriod.millis(0), TimePeriod.millis(1),tasks);
    }


    /**
     * Method which submits a task to the task collection.
     * The method creates a thread to handle said task.
     * @param taskType
     * @param tasks
     * @return
     */
    public synchronized ThreadManager submitThread(String name, WorkerThread.TaskType taskType, Period updateFrequency, Period startDelay, TaskWrapper... tasks){
    	threads.put(name, new WorkerThread(this, name, taskType, updateFrequency, startDelay, tasks));
    	return this;
	}

	public boolean containsTask(String task){
		return threads.contains(task);
	}

	public boolean containsTask(TaskWrapper task){
		return threads.containsValue(task);
	}

    /**
     * Method which removes specified thread from the collection
     */
    public synchronized void removeThread(String name){
    	threads.get(name).stop();
    	threads.remove(name);
    }
    /**
     * Method which starts of the threads created
     * by this thread manager.
     */
    public synchronized void startAll(){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().start();
		}
    }
    /**
     * Method which starts a thread at a specified index
     */
    public synchronized void start(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).start();
    }

    /**
     * Method which starts and joins all threads
     * @param join
     */
    public synchronized void startAll(boolean join){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().start();
		}
		if (join) {
			for(Entry<String, WorkerThread> entry: threads.entrySet()){
				entry.getValue().join();
			}
		}
    }

	public void setDaemon(String name, boolean state) {
		if(threads.size()<= 0)
			return;

		threads.get(name).setDaemon(state);
	}
    /**
     * Method which starts and joins a thread at a specified index
     */
    public synchronized void start(String name, boolean join){
     	if(threads.size()<= 0)
    		return;

     	threads.get(name).start();
    	if(join)
    		threads.get(name).join();
    }

    /**
     * Method which stops a thread at a specified index
     */
    public synchronized void stop(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).stop();
    }

    /**
     * Method which stops all threads
     */
    public synchronized void stopAll(){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().stop();
		}
    }

    /**
     * Method which resumes a thread at a specified index
     */
    public synchronized void resume(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).resume();
    }

    /**
     * Method which resumes all threads
     */
    public synchronized void resumeAll(){
    	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().resume();
		}
    }

    /**
     * Method which pauses a thread at a specified index
     */
    public synchronized void pause(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).pause();
    }

    /**
     * Method which pauses all threads
     */
    public synchronized void pauseAll(){
     	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().pause();
		}
    }

    /**
     * Method which makes a thread wait until notified
     * @param name
     */
    public synchronized void hold(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).pause();
    }

    /**
     * Method which makes all threads wait until notified
     */
    public synchronized void holdAll(){
     	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().hold();
		}
    }

    /**
     * Method which pauses a thread at a specified index
     * @param name
     */
    public synchronized void proceed(String name){
    	if(threads.size()<= 0)
    		return;

    	threads.get(name).proceed();
    }

    /**
     * Method which pauses all threads
     */
    public synchronized void proceedAll(){
     	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().proceed();
		}
    }

    /**
     * Method which adds task to the specified thread
     * @param task
     */
	public synchronized ThreadManager addTask(String thread, TaskWrapper task) {
		threads.get(thread).addTask(task);
		return this;
	}


    /**
     * Method which uses a task id to remove a thread from the collection
     * @param taskID
     */
    public synchronized void removeTask(String threadName, String taskID){
    	if(threads.size()<= 0)
    		return;

		threads.get(threadName).removeTask(taskID);
    }

	public synchronized void removeTask(WorkerThread workerThread) {
		threads.remove(workerThread);
	}

    /**
     * Method which checks if a thread at a specified index is running
     * @param name
     */
	public synchronized boolean isRunning(String name) {
		return threads.get(name).isRunning();
	}

	  /**
     * Method which gets the updates frequency of a thread at a specified index is running
     * @param name
     */
	public synchronized long getUpdateFrequency(String name) {
		if(threads.size()<= 0)
    		return 0;

		return threads.get(name).getUpdateFrequency();
	}

	  /**
     * Method which sets the updates frequency of a thread at a specified index is running
     * @param updateFrequency
     */
	public synchronized void setUpdateFrequency(long updateFrequency, String String) {
		if(threads.size()<= 0)
    		return;

		threads.get(String).setUpdateFrequency(updateFrequency);
	}

	  /**
     * Method which sets the updates frequency of all threads
     */
	public synchronized void setUpdateFrequencyForAll(int updateFrequency) {
		if(threads.size()<= 0)
    		return;

	  	for(Entry<String, WorkerThread> entry: threads.entrySet()){
			entry.getValue().setUpdateFrequency(updateFrequency);
		}
	}

	/**
	 * Method which removes all the threads
	 */
    public synchronized void clearThreads(){
    	if(threads.size()<= 0)
    		return;

    	stopAll();
    	threads.clear();
    }

    /**
     * Method which returns the current thread count
     * @return
     */
    public synchronized int threadCount(){
    	return threads.size();
    }

    public synchronized EventQueue addToEventQueue(Runnable runnable){
    	if(eventQueue == null){
    		eventQueue = new EventQueue();
    		eventQueue.start();
    	}
    	eventQueue.execute(runnable);
    	return eventQueue;
    }

    public synchronized void stopEventQueue(){
    	if(eventQueue!=null){
    		if(eventQueue.isRunning()){
    			eventQueue.stop();
    		}
    	}
    }

    /**
     * Static method which performs a given task
     * create a designated thread.
     * @param task
     */

    public static void performTask(TaskWrapper task){
        performTask("",task,null);
    }

    public static void performTask(TaskWrapper task, Runnable onFinished){
    	performTask("",task,onFinished);
    }

    public static void performTask(String name, TaskWrapper task, Runnable onFinished){

    	Worker worker = new Worker(task, onFinished);

    	Thread thread = new Thread(worker);

    	thread.setName(name);
    	thread.setDaemon(true);
    	thread.start();
    }

    public static WorkerThread createTask(WorkerThread.TaskType taskType, Period updateFrequency, Period startDelay, TaskWrapper task){
    	WorkerThread thread = new WorkerThread(null,"", taskType, updateFrequency, startDelay, task);
    	thread.setDaemon(true);
    	return thread;
	}

    
    public static void performTask(WorkerThread.TaskType taskType, Period updateFrequency, Period startDelay, TaskWrapper task){
    	WorkerThread thread = new WorkerThread(null,"", taskType, updateFrequency, startDelay, task);
    	thread.setDaemon(true);
    	thread.start();
	}

	public static void performScript(Runnable script) {
		Thread thread = new Thread(script);
		thread.start();
	}

	/**
     * Worker class used for performing tasks.
     * @author Eudy Contreras.
     *
     */
	private static class Worker implements Runnable {
		private TaskWrapper task;
        private Runnable onFinished;

        public Worker(TaskWrapper task, Runnable runnable) {
            this.task = task;
            this.onFinished = runnable;
        }

		@Override
		public void run() {
			task.doBackgroundWork();
            if(onFinished!=null){
                onFinished.run();
            }
		}
	}
	/**
	 * Static method computes a value create a
	 * designated thread.
	 * @param task
	 * @return
	 */
	public synchronized static <VALUE> VALUE computeValue(ValueTask<VALUE> task){
		return computeValue("",task);
	}


	public synchronized static <VALUE> VALUE computeValue(String name, ValueTask<VALUE> task){

    	ValueWorker<VALUE> worker = new ValueWorker<>(task);

    	Thread thread = new Thread(worker);

    	thread.setName(name);
    	thread.setDaemon(true);
    	thread.start();

    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    	return worker.getValue();
    }
	/**
	 * Worker class used for computing values.
	 * @author Eudy Contreras
	 *
	 * @param <VALUE>
	 */
    private static class ValueWorker<VALUE> implements Runnable {

	     private volatile VALUE value;
	     private volatile ValueTask<VALUE> task;

	     public ValueWorker(ValueTask<VALUE> task){
	    	 this.task = task;
	     }
	     @Override
	     public void run() {
	        value = task.computeValue();
	     }

	     public VALUE getValue() {
	    	 return value;
	     }
	 }
}
