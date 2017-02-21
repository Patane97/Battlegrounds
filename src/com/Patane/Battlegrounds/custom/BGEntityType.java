package com.Patane.Battlegrounds.custom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Creature;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.game.Game;
import com.Patane.Battlegrounds.custom.creatures.*;

import java.util.List;
import java.util.Set;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MinecraftKey;
import net.minecraft.server.v1_11_R1.RegistryMaterials;
import net.minecraft.server.v1_11_R1.World;

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
	
	String name;
	int id;
	Class<? extends Entity> clazz;
	
	private BGEntityType(String name, int id, Class <? extends Entity> custom){
		registerEntity(name, id, custom);
		this.name 	= name;
		this.id 	= id;
		this.clazz 	= custom;
	}
	public String getName(){
		return this.name;
	}
	public int getID(){
		return this.id;
	}
	public Class<? extends Entity> getEntityClass(){
		return this.clazz;
	}

	/**
     * Spawns custom entities via entityTypes
     *
     * (eg. spawnEntity(CustomEntityType.Zombie, location);)
     *
     * @param arena
     * @param entityType
     * @param loc
     * @return
     */
	public static org.bukkit.entity.Entity spawnEntity(Arena arena, BGEntityType entityType, Location loc){
		return spawnEntity(arena, entityType.getName(), entityType.getEntityClass(), loc);
	}
	/**
     * Spawns custom entities via class
    *
    * (eg. spawnEntity("Fast Zombie", FastZombie.class, location);)
    *
    * @param arena
    * @param clazz
    * @param loc
    * @return
    */
	public static org.bukkit.entity.Entity spawnEntity(Arena arena, String name, Class<? extends Entity> clazz, Location loc){
		Entity entity = null;
		// creates new instance of entity from given Class<? extends Entity>. Returns null if failed.
		try { 
			CraftWorld world = (CraftWorld) loc.getWorld();
			entity = (Entity) clazz.getConstructor(World.class).newInstance(world.getHandle());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		entity.setCustomName(name);
		// grabbing a bukkit version of the entity
		org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
		
		// if arena is in Game Mode, adds the creature to activeCreatures list
		if(arena != null && arena.getMode() instanceof Game && bukkitEntity instanceof Creature)
			((Game) arena.getMode()).getRoundHandler().getActiveCreatures().add((Creature) bukkitEntity);
		
		// spawns entity at loc
		entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
		
		// checks if entity has an "onSpawn" method and runs it if so.
		try { 
			Method method = clazz.getDeclaredMethod("onSpawn");
			method.setAccessible(true);
			method.invoke(entity);
			}
		catch (Exception e) {}
		return bukkitEntity;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void registerEntity(String name, int id, Class<? extends Entity> customClass){
		MinecraftKey key = new MinecraftKey(name);
		try {
			((RegistryMaterials) getPrivateStatic(EntityTypes.class, "b")).a(id, key, customClass);
			((Set) getPrivateStatic(EntityTypes.class, "d")).add(key);
			((List) getPrivateStatic(EntityTypes.class, "g")).set(id, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private static Object getPrivateStatic(Class clazz, String f) throws Exception{
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	}
}
