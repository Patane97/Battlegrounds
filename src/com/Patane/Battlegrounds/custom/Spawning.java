package com.Patane.Battlegrounds.custom;

import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Creature;

import com.Patane.Battlegrounds.arena.Arena;
import com.Patane.Battlegrounds.arena.game.Game;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.World;

public class Spawning {
	/**
     * Spawns custom entities via entityTypes
     *
     * (eg. spawnEntity(null, CustomEntityType.Zombie, location);)
     *
     * @param arena
     * @param entityType
     * @param loc
     * @return
     */
	public static org.bukkit.entity.Entity spawnEntity(Arena arena, BGEntityType entityType, Location loc){
		BGEntityType.registerEntity(entityType.getName(), entityType.getID(), entityType.getEntityClass());
		org.bukkit.entity.Entity entity = spawnEntity(arena, entityType.getName(), entityType.getEntityClass(), loc);
		BGEntityType.unregisterEntity(entityType.getID());
		return entity;
	}
	
	private static org.bukkit.entity.Entity spawnEntity(Arena arena, String name, Class<? extends Entity> clazz, Location loc){
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
}
