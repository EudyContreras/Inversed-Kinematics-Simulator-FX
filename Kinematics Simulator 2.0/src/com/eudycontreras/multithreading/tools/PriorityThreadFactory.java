package com.eudycontreras.multithreading.tools;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Eudy Contreras on 10/14/2016.
 */


public class PriorityThreadFactory implements ThreadFactory {

    private final int threadPriority;

    private Thread thread;

    public PriorityThreadFactory(int threadPriority) {
    	this.threadPriority = threadPriority;
    }

    @Override
    public Thread newThread(final Runnable runnable) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread.setPriority(threadPriority);
                } catch (Throwable t) {
                }
                runnable.run();
            }
        });
        return thread;
    }
}