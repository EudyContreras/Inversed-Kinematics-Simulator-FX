package com.utils;

import java.util.Random;

public abstract class RandomGenUtility {
	public static Random rand = new Random();
  
	//Gets a random number, from 0 to maxValue. exclusive.
	public static double getERNG(double maxValue) {
		return rand.nextDouble()*(maxValue);
	}

	//Gets a random number, from 0 to maxValue. inclusive.
	public static double getRNG(double maxValue) {
		return rand.nextDouble()*(maxValue + 1);
	}

	//Gets a random double number in range. inclusive.
	public static double getRandom(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
	}

	//Gets a random float number in range. inclusive.
	public static float getRandom(float minValue, float maxValue) {
		return rand.nextFloat()*(maxValue + 1 - minValue) + minValue;
	}

	//Gets a random int number in range. inclusive.
	public static int getRandom(int minValue, int maxValue) {
		return rand.nextInt(maxValue + 1 - minValue) + minValue;
	}
}
