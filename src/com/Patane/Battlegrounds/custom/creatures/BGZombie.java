package com.Patane.Battlegrounds.custom.creatures;

import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntityZombie;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.World;

public class BGZombie extends EntityZombie{

	public BGZombie(World world) {
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
}
