package com.eudycontreras.multithreading.tools;

/**
 * Created by Eudy Contreras on 10/14/2016.
 */

public class PriorityRunnable implements Runnable {

    private final TaskPriority threadPriority;

    public PriorityRunnable(TaskPriority threadPriority) {
        this.threadPriority = threadPriority;
    }

    @Override
    public void run() {}

    public TaskPriority getThreadPriority() {
        return threadPriority;
    }

}