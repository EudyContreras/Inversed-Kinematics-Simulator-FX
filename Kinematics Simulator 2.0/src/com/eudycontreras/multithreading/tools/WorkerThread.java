package com.eudycontreras.multithreading.tools;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.eudycontreras.multithreading.managers.ThreadManager;
import com.eudycontreras.multithreading.timers.Period;
import com.eudycontreras.multithreading.timers.TimePeriod;


/**
 * Class which represents a worker. This class
 * creates a thread and assigns a valueTask to the thread.
 * The functions of this class are controlled by a Thread Manager.
 * Created by Eudy Contreras on 10/14/2016.
 */
public class WorkerThread{

	private String name;

	private ThreadManager manager;
	private WorkerThreadHelper workerHelper;

	private LinkedHashMap<String,TaskWrapper> computeTask;

	private volatile boolean onHold = false;
	private volatile boolean parallel = false;
	private volatile boolean running = false;
	private volatile boolean active = false;

	private TaskType taskType  = TaskType.CONTINUOUS;

	private volatile int taskCounter = 0;
	private volatile long updateFrequency = 0;
	private volatile long startDelay = 0;

	public WorkerThread(ThreadManager manager, String name, Period updateFrequency, TaskWrapper... update) {
		this(manager, name, TaskType.CONTINUOUS,updateFrequency, TimePeriod.millis(0),update);
	}

	public WorkerThread(ThreadManager manager, String name, TaskType taskType, Period updateFrequency, TaskWrapper... update) {
		this(manager, name, taskType,updateFrequency, TimePeriod.millis(0),update);
	}

	/**
	 * Constructor which creates a worker thread and sets all the initial values.
	 * @param manager The manager of this worker
	 * @param taskType The type of valueTask: Continuous or Single instance
	 * @param updateFrequency The frequency in which the updates will be made
	 * @param startDelay The delay before the first updates is performed
	 * @param update The valueTask to be performed by this thread
	 */
	public WorkerThread(ThreadManager manager, String name, TaskType taskType, Period updateFrequency, Period startDelay, TaskWrapper... update) {
		this.computeTask = addTasks(update);
		this.name = name;
		this.taskType = taskType;
		this.manager = manager;
		this.updateFrequency = updateFrequency.getDuration();
		this.startDelay = startDelay.getDuration();
	}

	private LinkedHashMap<String,TaskWrapper> addTasks(TaskWrapper...taskWrappers){
		computeTask = new LinkedHashMap<>();
		for(TaskWrapper wrapper: taskWrappers){
			taskCounter++;
			if(wrapper instanceof TaskUpdate){
				TaskUpdate update = (TaskUpdate)wrapper;
				if(update.getTaskID()!=null){
					computeTask.put(update.getTaskID(), update);
				}else{
					computeTask.put(String.valueOf(taskCounter), update);
				}
			}else{
				computeTask.put(String.valueOf(taskCounter), wrapper);
			}
		}
		return computeTask;
	}
	/**
	 * Method which creates and start the thread
	 * used by this class
	 */
	public void start() {
		if (workerHelper == null) {
			workerHelper = null;
			workerHelper = new WorkerThreadHelper();
			workerHelper.setDaemon(true);
			workerHelper.setName(name);
			workerHelper.start();
			running = true;
			active = true;
		}
	}
	/**
	 * Method which stops the thread of this class
	 */
	public void stop() {
		if (workerHelper != null) {
			active = false;
			running = false;
			workerHelper.interrupt();
			workerHelper = null;
		}
	}

	/**
	 * Method which joins this this thread
	 */
	public void join() {
		if (workerHelper != null) {
			try {
				workerHelper.join();
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * Method which joins this thread
	 */
	public void setDaemon(boolean state) {
		if(workerHelper == null)
			return;

		workerHelper.setDaemon(state);
	}
	/**
	 * Method which resumes the thread
	 */
	public void resume() {
		running = true;
	}

	/**
	 * Method which pauses the thread
	 */
	public void pause() {
		running = false;
	}

	/**
	 * Method which returns whether the thread is running or not
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Method which returns whether the thread is alive or not
	 * @return
	 */
	public boolean isAlive(){
		return active;
	}

	/**
	 * Method which makes the thread wait if not
	 * already waiting.
	 */
	public void hold(){
		if(workerHelper!=null){
			if(!isOnHold()){
				try {
					workerHelper.wait();
					onHold = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Method which allows this thread to
	 * proceed if it is waiting
	 */
	public void proceed(){
		if(workerHelper!=null){
			if(isOnHold()){
				workerHelper.notify();
				onHold = false;
			}
		}
	}

	/**
	 * Returns whether this thread is on hold or not
	 * @return
	 */
	public boolean isOnHold(){
		return onHold;
	}
	/**
	 * Method which returns the updates frequency of this thread
	 * @return
	 */
	public long getUpdateFrequency() {
		return updateFrequency;
	}

	/**
	 * Method which sets the updates frequency of this thread
	 * @param updateFrequency
	 */
	public void setUpdateFrequency(long updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	/**
	 * Method which returns the ID of the valueTask being performed by the thread
	 * @return
	 */
	public String getThreadName(){
		return workerHelper.getName();

	}

	/**
	 * Method which returns the valueTask being performed by this thread
	 * @return
	 */
	public TaskWrapper getTask(String name) {
		return computeTask.get(name);
	}

	/**
	 * Method which adds a new valueTask to be performed by the thread.
	 * @param update
	 */
	public void addTask(TaskWrapper update) {
		if(update instanceof TaskUpdate){
			TaskUpdate task = (TaskUpdate)update;
			if(task.getTaskID()!=null){
				computeTask.put(task.getTaskID(), update);
			}else{
				computeTask.put(String.valueOf(taskCounter), update);
			}
		}else{
			computeTask.put(String.valueOf(taskCounter), update);
		}
	}

	/**
	 * Method which removes valueTask from the valueTask collection
	 * @param name
	 * @return
	 */
	public boolean removeTask(String name){
		if(computeTask.containsKey(name)){
			computeTask.remove(name);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Method which a Thread sleep time.
	 * @param milliseconds
	 */
	private void threadSleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
		}
	}
	/**
	 * Method which performs an updates to the valueTask
	 */
	private void performUpdate() {
		if (parallel) {
			computeTask.entrySet().parallelStream().forEach(entry -> {
				TaskWrapper value = entry.getValue();

				if (value != null) {
					value.doBackgroundWork();
				}
			});

		} else {
			for(Entry<String,TaskWrapper> entry : computeTask.entrySet()){
				entry.getValue().doBackgroundWork();
			}
		}
	}

	/**
	 * Thread class used by the worker to perform a valueTask.
	 * @author Eudy Contreas
	 *
	 */
	public class WorkerThreadHelper extends Thread {

		public void run() {

			switch(taskType){
			case CONTINUOUS:

				threadSleep(startDelay);

				while (running) {

					performUpdate();
					threadSleep(updateFrequency);
				}
				break;
			case SINGLE_INSTANCE:

				performUpdate();
				remove();

				break;
			default:
				break;

			}
		}
	}

	/**
	 * Method which removes this worker from the manager.
	 */
	private void remove(){
		if(manager!=null)
		manager.removeTask(this);
	}

	/**
	 * Enumeration containing valueTask types
	 * @author Eudy Contreras
	 *
	 */
	public enum TaskType{
		CONTINUOUS, SINGLE_INSTANCE
	}
}
