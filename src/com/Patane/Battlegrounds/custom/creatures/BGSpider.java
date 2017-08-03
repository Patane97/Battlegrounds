package com.Patane.Battlegrounds.custom.creatures;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityIronGolem;
import net.minecraft.server.v1_12_R1.EntitySpider;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.World;

public class BGSpider extends EntitySpider {

	public BGSpider(World world) {
		super(world);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (damagesource.getEntity() instanceof EntityCreature) {
			return false;
		}
		return super.damageEntity(damagesource, f);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void r()
	  {
	    this.goalSelector.a(1, new PathfinderGoalFloat(this));
	    this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
	    this.goalSelector.a(4, new PathfinderGoalSpiderMeleeAttack(this));
	    this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 0.8D));
	    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	    this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
	    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
	    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityIronGolem.class, true));
	  }
	static class PathfinderGoalSpiderMeleeAttack
    extends PathfinderGoalMeleeAttack
  {
    public PathfinderGoalSpiderMeleeAttack(EntitySpider entityspider)
    {
      super(entityspider, 1.0D, true);
    }
    
//    public boolean b()
//    {
//      float f = this.b.e(1.0F);
//      if ((f >= 0.5F) && (this.b.getRandom().nextInt(100) == 0))
//      {
//        this.b.setGoalTarget(null);
//        return false;
//      }
//      return super.b();
//    }
//    
//    protected double a(EntityLiving entityliving)
//    {
//      return 4.0F + entityliving.width;
//    }
  }
}
