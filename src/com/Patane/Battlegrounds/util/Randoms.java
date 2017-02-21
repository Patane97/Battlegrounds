package com.Patane.Battlegrounds.util;

import java.util.Random;

public class Randoms {
	/**
	 * Generates a random number between max and min
	 * 
	 * @param max
	 * @param min
	 * @return a random integer between max and min
	 */
	public static int integer(int min, int max){
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}
}
