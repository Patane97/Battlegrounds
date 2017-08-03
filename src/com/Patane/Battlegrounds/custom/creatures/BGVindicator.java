package com.Patane.Battlegrounds.custom.creatures;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityVindicator;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.World;

public class BGVindicator extends EntityVindicator{

	public BGVindicator(World world) {
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
