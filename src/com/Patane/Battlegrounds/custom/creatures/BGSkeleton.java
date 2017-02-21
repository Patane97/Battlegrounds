package com.Patane.Battlegrounds.custom.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntitySkeleton;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.World;

public class BGSkeleton extends EntitySkeleton{

	public BGSkeleton(World world) {
		super(world);
	}
	@Override
	protected void initAttributes(){
		super.initAttributes();
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
		Skeleton skeleton = (Skeleton) bukkitEntity;
		skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
	}
}
