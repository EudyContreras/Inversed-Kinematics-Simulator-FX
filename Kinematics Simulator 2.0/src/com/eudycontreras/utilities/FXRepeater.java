package com.eudycontreras.utilities;

public class FXRepeater{

	public static final IRepeater iterations(int times){
		return new Repetitions(times);
	}
 
	public static void repeat(int times, RepeatWrapper repeater){
		for(int i = 0; i<times; repeater.repeat(i), i++);
	}

	public static void repeat(int start, int end, RepeatWrapper repeater){
		for(int i = start; i<end; repeater.repeat(i), i++);
	}

	public interface RepeatWrapper{
		public void repeat(int index);
	}

	private static class Repetitions implements IRepeater{
		private int times;

		public Repetitions(int times){
			this.times = times;
		}

		@Override
		public int getRepeats() {
			return times;
		}
	}
	
	public interface IRepeater {

		public int getRepeats();

	}
}