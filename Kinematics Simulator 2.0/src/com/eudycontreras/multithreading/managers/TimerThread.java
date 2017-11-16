package com.eudycontreras.multithreading.managers;


import com.eudycontreras.multithreading.timers.Period;

import javafx.application.Platform;

public class TimerThread {

	public static void schedule( final Period delay, final Runnable script){
		ThreadManager.performScript(() -> {
            waitTime(delay.getDuration());
            Platform.runLater(script);
        });
	}

	public static void scheduleAtRate( final Period delay, final Period rate, final Runnable script){
		ThreadManager.performScript(() -> {
			waitTime(delay.getDuration());
			while(true){
				waitTime(rate.getDuration());
				script.run();
			}
		});
	}

	public static TimerControl scheduleAtControlledRate( final Period delay, final Period rate, final Runnable script){
		TimerControl timerControl = new TimerControl();
		ThreadManager.performScript(() -> {
			waitTime(delay.getDuration());
			while(timerControl.isRunTimer()){
				waitTime(rate.getDuration());
				script.run();
			}
		});
		return timerControl;
	}

	public static void intervalIterate(final int start, final int count, final Period period, final Period delay, final IterateWrapper wrapper){
		ThreadManager.performScript(() -> {
			waitTime(delay.getDuration());
			int counter = start;
			while(counter<=count){
				wrapper.onIteration(counter);
				waitTime(period.getDuration());
				counter++;
			}
			wrapper.onComplete();
		});
	}


	private static void waitTime(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public interface IterateWrapper{
		void onIteration(int index);
		void onComplete();
	}

	public static class TimerControl{
		private boolean runTimer = true;

		public boolean isRunTimer() {
			return runTimer;
		}

		public void setRunTimer(boolean runTimer) {
			this.runTimer = runTimer;
		}
	}

}
