package com.eudycontreras.multithreading.timers;

import java.util.concurrent.TimeUnit;

public class TimePeriod {


	public static final Period ZERO = new Zero();
	
	public static final Period hours(int time){
		return new Hour(time);
	}

	public static final Period minutes(int time){
		return new Minute(time);
	}

	public static final Period seconds(int time){
		return new Second(time);
	}

	public static final Period millis(int time){
		return new MilliSecond(time);
	}

	private static class Hour implements Period {

		private long time;

		public Hour(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.HOURS,time);
		}

	}

	private static class Minute implements Period {
		private long time;

		public Minute(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.MINUTES,time);
		}

	}

	private static class Second implements Period {
		private long time;

		public Second(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.SECONDS,time);
		}

	}

	private static class MilliSecond implements Period {
		private long time;

		public MilliSecond(long time) {
			this.time = time;
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.MILLISECONDS,time);
		}

	}

	private static class Zero implements Period {
		private long time = 0;

		public Zero() {
		}

		@Override
		public long getDuration() {
			return getAbsoluteTime(TimeUnit.MILLISECONDS,time);
		}

	}

	private static final long getAbsoluteTime(TimeUnit unit, long time){
		switch(unit){
			case HOURS:
				return ((time*1000)*60)*60;
			case MINUTES:
				return ((time*1000)*60);
			case SECONDS:
				return (time*1000);
			case MILLISECONDS:
				return time;
			default:
				break;
		}
		return time;
	}
}
