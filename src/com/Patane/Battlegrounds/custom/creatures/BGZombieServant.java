package com.Patane.Battlegrounds.custom.creatures;

import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityCreature;
import net.minecraft.server.v1_11_R1.EntityMonster;
import net.minecraft.server.v1_11_R1.EntityPigZombie;
import net.minecraft.server.v1_11_R1.EntityZombie;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_11_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_11_R1.World;

public class BGZombieServant extends EntityZombie{

	public BGZombieServant(World world) {
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void dk(){
	    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
	    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityMonster.class, true));
	}
}
