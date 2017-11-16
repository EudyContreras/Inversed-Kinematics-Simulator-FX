package com.eudycontreras.multithreading.tools;

/**
 * Task container class which can be used to wrap task
 * and can also assign and ID to a wrapped task.
 * Created by Eudy Contreras on 10/14/2016.
 *
 */
public class TaskUpdate implements TaskWrapper {

	private String taskID;

	public TaskUpdate(){this(null);}

	public TaskUpdate(String taskID){this.taskID = taskID;}

	public String getTaskID() {return taskID;}

	public void setTaskID(String taskID) {this.taskID = taskID;}

	@Override
	public void doBackgroundWork() {}

}
