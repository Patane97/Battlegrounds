package com.Patane.Battlegrounds.util;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.entity.EntityType;

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

	/**
	 * Chooses a random EntityType from the array of allowedTypes
	 * 
	 * @param allowedTypes
	 * @return a random EntityType from allowedTypes OR null if allowedTypes is empty
	 */
	public static EntityType entityType(ArrayList<EntityType> allowedTypes){
		if(allowedTypes.isEmpty()) return null;
		int max = allowedTypes.size()-1;
		int min = 0;
		int randomNumber = Randoms.integer(min, max);
		
		EntityType randomEntityType = allowedTypes.get(randomNumber);
		return randomEntityType;
	}
}
