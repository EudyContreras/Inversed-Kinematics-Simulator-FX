package com.eudycontreras.javafx.fbk.utilities;

import static java.lang.Math.random;
import static java.lang.Math.round;
import java.util.Random;

/**
 * Utility class for generating a range of random values
 * 
 * @author Eudy Contreras
 *
 */
public abstract class RandomGenUtility {

	public static Random rand = new Random();

	private static final char[] symbols;

	private static char[] buffer;

	static {
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();

	}

	public static double getERNG(double maxValue) {
		return rand.nextDouble() * (maxValue);
	}

	public static double getRNG(double maxValue) {
		return rand.nextDouble() * (maxValue + 1);
	}

	public static double getRandom(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
	}

	public static float getRandom(float minValue, float maxValue) {
		return rand.nextFloat() * (maxValue + 1 - minValue) + minValue;
	}

	public static int getRandom(int minValue, int maxValue) {
		return rand.nextInt(maxValue + 1 - minValue) + minValue;
	}

	public static boolean getRandBool() {
		return rand.nextBoolean();
	}

	public static double getRandom(double variation) {
		return (random() * variation * 2 + 1 - variation);
	}

	public static double getGaussianRandom(double mean, double deviation) {
		return mean + deviation * rand.nextGaussian();
	}

	public static double getGaussianRandom(double from, double to, double mean, double deviation) {
		double result = 0;
		do {
			result = getGaussianRandom(mean, deviation);
		} while (result < from || result > to);
		return result;
	}

	public static int getRandomIndex(int from, int to) {
		return (int) round(random() * (to - from)) + from;
	}

	public static String getRandomString(int length) {
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buffer = new char[length];
		return nextString();
	}

	private static String nextString() {
		for (int idx = 0; idx < buffer.length; ++idx)
			buffer[idx] = symbols[rand.nextInt(symbols.length)];
		return new String(buffer);
	}
}
