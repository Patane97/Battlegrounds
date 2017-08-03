package com.Patane.Battlegrounds.custom;

import net.minecraft.server.v1_12_R1.Entity;

public class oldMobEntry {
	private final String name;
	private final Class<? extends Entity> clazz;
	
	public oldMobEntry(String name, Class<? extends Entity> clazz) {
        this.name = name;
        this.clazz = clazz;
    }
	public String retrieveName(){
		return name;
	}
	public Class<? extends Entity> retrieveClass(){
		return clazz;
	}
}
