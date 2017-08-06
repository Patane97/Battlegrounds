package com.Patane.Battlegrounds.GUI;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.ContainerAnvil;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;

public abstract class AnvilGUI extends GUI{
	AnvilInventory inventory;
	
	public AnvilGUI(Plugin plugin, Player player, String name) {
		super(plugin, player, null);
		inventory = (AnvilInventory) Anvil.openFakeInventory(player);
		setListener(new AnvilListener(plugin));
	}
	/**
	 * 
	 * @param inventory
	 * @param result
	 * @return ItemStack to set as Result, Null to not change result
	 */
	public abstract ItemStack prepareAnvil(AnvilInventory inventory, ItemStack result);
	
	public abstract boolean regularClick(AnvilInventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot);
	@Override
	public boolean regularClick(Inventory inventory, ClickType click, ItemStack clickedItem, ItemStack cursorItem, int slot) {
		return false;
	}
	public static class Slot {
        public static final int INPUT_LEFT = 0;
        public static final int INPUT_RIGHT = 1;
        public static final int OUTPUT = 2;
    }
	protected class AnvilListener extends Listener {
		public AnvilListener(Plugin plugin) {
			super(plugin);
		}
		@EventHandler
		public void onPrepare(PrepareAnvilEvent event){
			if(!(event.getInventory().equals(inventory) || event.getView().getTopInventory().equals(inventory)))
				return;
			AnvilInventory thisInventory = (AnvilInventory) event.getInventory();
			ItemStack result = event.getResult();
			ItemStack tempResult = prepareAnvil(thisInventory, result);
			if(tempResult != null)
				event.setResult(tempResult);
		}
		@Override
		@EventHandler
		public void onItemClick(InventoryClickEvent event){
			if(!(event.getInventory().equals(inventory) || event.getView().getTopInventory().equals(inventory)))
				return;
			AnvilInventory thisInventory = (AnvilInventory) event.getInventory();
			ItemStack clickedItem 	= (event.getCurrentItem() == null ? new ItemStack(Material.AIR) : event.getCurrentItem());
			ItemStack cursorItem 	= (event.getCursor() == null ? new ItemStack(Material.AIR) : event.getCursor());
			int slot = event.getRawSlot();
			ClickType click = event.getClick();
			
			event.setCancelled(regularClick(thisInventory, click, clickedItem, cursorItem, slot));
		}
	}
	
	/*
	 * Credit to lewysryan: https://bukkit.org/threads/1-8-open-an-anvil-inventory-not-much-code.328178/
	 */
	public static class Anvil {
	    public static Inventory openFakeInventory(Player player) {
	   
	        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
	        FakeAnvil fakeAnvil = new FakeAnvil(entityPlayer);
	        int containerId = entityPlayer.nextContainerCounter();
	   
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));
	   
	        entityPlayer.activeContainer = fakeAnvil;
	        entityPlayer.activeContainer.windowId = containerId;
	        entityPlayer.activeContainer.addSlotListener(entityPlayer);
	        entityPlayer.activeContainer = fakeAnvil;
	        entityPlayer.activeContainer.windowId = containerId;
	   
	        return fakeAnvil.getBukkitView().getTopInventory();
	    }
	}
	public static final class FakeAnvil extends ContainerAnvil {

		public FakeAnvil(EntityHuman entityHuman) {    
		    super(entityHuman.inventory, entityHuman.world, new BlockPosition(0,0,0), entityHuman);
		}
		@Override
		public boolean a(EntityHuman entityHuman) {    
			return true;
		}
	}
}
