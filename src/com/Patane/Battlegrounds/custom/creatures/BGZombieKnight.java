package com.Patane.Battlegrounds.custom.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.PathfinderGoalZombieAttack;
import net.minecraft.server.v1_12_R1.World;

public class BGZombieKnight extends EntityZombie{

	public BGZombieKnight(World world) {
		super(world);
	}
	@Override
	protected void initAttributes(){
		super.initAttributes();
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.18D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D);
	}
	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		if (damagesource.getEntity() instanceof EntityCreature){
			return false;
			}
		return super.damageEntity(damagesource, f);
	}
	public void onSpawn(){
		Zombie zombie = (Zombie) bukkitEntity;
		EntityEquipment	ee = zombie.getEquipment();
		ee.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		ee.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		ee.setBoots(new ItemStack(Material.IRON_BOOTS));
		ee.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
	}
	@Override
	protected void r()
	  {
	    this.goalSelector.a(0, new PathfinderGoalFloat(this));
	    this.goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0D, false));
	    this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
	    this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
	    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	    dk();
	  }
}
