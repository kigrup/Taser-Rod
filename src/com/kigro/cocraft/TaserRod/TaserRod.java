package com.kigro.cocraft.TaserRod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class TaserRod extends JavaPlugin {
	

	public ItemStack taserItem;
	
	List<TaserInteraction> interactions = new ArrayList<TaserInteraction>();
	
	@Override
	public void onEnable() {
		// Create taser
		CreateTaser();
		
		// Register event listener
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		
		// Register commands
		this.getCommand("taser").setExecutor((CommandExecutor) new CommandTaser());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void CreateTaser() {
		// Create taser
				ItemStack taser = new ItemStack(Material.FISHING_ROD);
				ItemMeta taserMeta = taser.getItemMeta();
				
				taserMeta.setDisplayName("Taser");
				
				List<String> lore = new ArrayList<String>();
				
				lore.add(ChatColor.GRAY + "Uso policial exclusivo");
				
				String ref = "Ref# 302495";
				lore.add(ChatColor.YELLOW + ref);
				
				lore.add(ChatColor.DARK_GRAY + (ChatColor.ITALIC + "Made in Minecraftia"));
				
				taserMeta.setLore(lore);
				
				taser.setItemMeta(taserMeta);
				
				taserItem = taser;
	}
	
	public Boolean isTaser(ItemStack itemA) {

		ItemMeta metaA = itemA.getItemMeta();
		ItemMeta metaB = taserItem.getItemMeta();

		if (metaA == null || metaB == null)
			return false;
		else if (metaA.getDisplayName() == null || metaA.getLore() == null) {
			return false;
		}
		
		Boolean sameName = metaA.getDisplayName().equals(metaB.getDisplayName()) ? true : false;
		Boolean sameLore = metaA.getLore().equals(metaB.getLore()) ? true : false;

		if (sameName && sameLore) {
			return true;
		}
		else {
			return false;
		}
	}
}