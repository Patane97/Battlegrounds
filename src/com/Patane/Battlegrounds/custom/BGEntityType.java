package com.Patane.Battlegrounds.custom;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.custom.creatures.BGChaserSkeleton;
import com.Patane.Battlegrounds.custom.creatures.BGChicken;
import com.Patane.Battlegrounds.custom.creatures.BGCreeper;
import com.Patane.Battlegrounds.custom.creatures.BGSkeleton;
import com.Patane.Battlegrounds.custom.creatures.BGSpider;
import com.Patane.Battlegrounds.custom.creatures.BGVindicator;
import com.Patane.Battlegrounds.custom.creatures.BGZombie;
import com.Patane.Battlegrounds.custom.creatures.BGZombieKnight;
import com.Patane.Battlegrounds.custom.creatures.BGZombieServant;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MinecraftKey;
import net.minecraft.server.v1_11_R1.RegistryMaterials;

public enum BGEntityType {
	// NAME("Entity name", Entity ID, yourcustomclass.class);
	ZOMBIE("Zombie", 54, BGZombie.class),
	SKELETON("Skeleton", 51, BGSkeleton.class),
	SPIDER("Spider", 52, BGSpider.class),
	CREEPER("Creeper", 50, BGCreeper.class),
	CHASER_SKELETON("Chaser Skeleton", 51, BGChaserSkeleton.class),
	ZOMBIE_KNIGHT("Zombie Knight", 54, BGZombieKnight.class),
	VINDICATOR("Vindicator", 36, BGVindicator.class),
	CHICKEN("Chicken", 93, BGChicken.class),
	ZOMBIE_SERVANT("Zombie Servant", 54, BGZombieServant.class);

	 // <ID, oldName&oldClazz>
	static HashMap<Integer, oldMobEntry> overriddenMobs = new HashMap<Integer, oldMobEntry>();
	
	int id;
	
	String name;
	Class<? extends Entity> clazz;
	
	private BGEntityType(String name, int id, Class <? extends Entity> custom){
		this.name 	= name;
		this.id 	= id;
		this.clazz 	= custom;
	}
	public int getID(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public Class<? extends Entity> getEntityClass(){
		return this.clazz;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static void registerEntity(String name, int id, Class<? extends Entity> customClass){
		MinecraftKey key = new MinecraftKey(name);
		try {
			RegistryMaterials b = ((RegistryMaterials) getPrivateStatic(EntityTypes.class, "b"));
			Set d = ((Set) getPrivateStatic(EntityTypes.class, "d"));
			List g = ((List) getPrivateStatic(EntityTypes.class, "g"));
			
			Class<? extends Entity> oldClass = (Class<? extends Entity>) b.getId(id);
//			MinecraftKey oldKey = (MinecraftKey) ((RegistryMaterials) getPrivateStatic(EntityTypes.class, "b")).b(oldClass);
			String oldName = (String) g.get(id);
//			Messenger.info("ID: " + id + " | Name: " + oldName + " | Class: " + oldClass.getName());
			oldMobEntry old = new oldMobEntry(oldName, oldClass);
			
			if(overriddenMobs == null) overriddenMobs = new HashMap<Integer, oldMobEntry>();
			
			overriddenMobs.put(id, old);
			b.a(id, key, customClass);
			d.add(key);
			g.set(id, name);
//			Messenger.broadcast("&7Registered Mob " + name);
		} catch (Exception e) {
			Messenger.warning("Failed to register Mob!");
			e.printStackTrace();
		}
	}
	static void unregisterEntity(int id){
		oldMobEntry old = overriddenMobs.get(id);
		try{
//			Messenger.broadcast("&7Restoring Mob " + old.retrieveName());
			registerEntity(old.retrieveName(), id, old.retrieveClass());
			overriddenMobs.remove(id);
		} catch (NullPointerException e){}
	}

	@SuppressWarnings("rawtypes")
	private static Object getPrivateStatic(Class clazz, String f) throws Exception{
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	}
}
