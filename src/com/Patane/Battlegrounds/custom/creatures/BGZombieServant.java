package com.Patane.Battlegrounds.custom.creatures;

import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityAnimal;
import net.minecraft.server.v1_11_R1.EntityMonster;
import net.minecraft.server.v1_11_R1.EntityPigZombie;
import net.minecraft.server.v1_11_R1.EntityPlayer;
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
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3D);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(5.0D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D);
	}
	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		if (damagesource.getEntity() instanceof EntityPlayer || damagesource.getEntity() instanceof EntityAnimal){
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
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	protected void r()
//	  {
//	    this.goalSelector.a(0, new PathfinderGoalFloat(this));
//	    this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, false));
//	    this.goalSelector.a(6, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
//	    this.goalSelector.a(8, new PathfinderGoalRandomStrollLand(this, 1.0D));
//	    this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//	    this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));
//	    this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
//	    this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
//	    this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
//	    this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget(this, EntityMonster.class, false));
//	  }
//	@Override
//	public EntityAgeable createChild(EntityAgeable arg0) {
//		return null;
//	}
}
