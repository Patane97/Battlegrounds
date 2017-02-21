package com.Patane.Battlegrounds.custom.creatures;

import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntitySkeleton;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.World;

public class BGChaserSkeleton extends EntitySkeleton{

	public BGChaserSkeleton(World world) {
		super(world);
	}
	@Override
	protected void initAttributes(){
		super.initAttributes();
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D);
	}
	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		if (damagesource.getEntity() instanceof EntityCreature){
			return false;
			}
		return super.damageEntity(damagesource, f);
	}
}
