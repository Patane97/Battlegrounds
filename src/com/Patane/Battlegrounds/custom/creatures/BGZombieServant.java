package com.Patane.Battlegrounds.custom.creatures;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityAnimal;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.World;

public class BGZombieServant extends EntityZombie{

	public BGZombieServant(World world) {
		super(world);
	}
	@Override
	protected void initAttributes(){
		super.initAttributes();
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3D);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D);
		getAttributeInstance(GenericAttributes.maxHealth).setValue(5.0D);
	}
	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		if (damagesource.getEntity() instanceof EntityPlayer || damagesource.getEntity() instanceof EntityAnimal){
			return false;
			}
		return super.damageEntity(damagesource, f);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	protected void dk(){
//	    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
//	    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityMonster.class, true));
//	}
	public void onSpawn(){
		Zombie zombie = (Zombie) bukkitEntity;
		EntityEquipment	ee = zombie.getEquipment();
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
		chestplateMeta.setColor(Color.fromRGB(150, 255, 0));
		chestplate.setItemMeta(chestplateMeta);
		ee.setChestplate(chestplate);
//		ee.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
//		ee.setBoots(new ItemStack(Material.IRON_BOOTS));
//		ee.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
	}
}
