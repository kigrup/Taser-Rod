package com.kigro.cocraft.TaserRod;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {

	TaserRod main = JavaPlugin.getPlugin(TaserRod.class);
	
	// An array that holds TaserInteraction objects.
	// There should be only one object per tased player
	List<TaserInteraction> interactions = main.interactions;
	
	
	// Catch player with taser hook
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity Projectile = event.getEntity();
		if (Projectile instanceof Arrow) {
			for (TaserInteraction ti : interactions) {
				Bukkit.broadcastMessage(ti.getAttacker().getName());
			}
		}
		if (Projectile instanceof FishHook) {
			
			Player attacker = (Player)((FishHook) Projectile).getShooter();
			ItemStack mainHand = attacker.getInventory().getItemInMainHand();
			ItemStack offHand = attacker.getInventory().getItemInOffHand();

			// check if taser in any hand
			Boolean mainHandTaser = (mainHand == null)? false : main.isTaser(mainHand);
			Boolean offHandTaser = (offHand == null)? false : main.isTaser(offHand);

			if (mainHandTaser || offHandTaser) {
				Entity hit = event.getHitEntity();
				if (hit instanceof Player) {
					TaserInteraction ti = new TaserInteraction(attacker, hit);
					interactions.add(ti);
					
					ti.Tase();
					
					// Particles
					Particle par = Particle.VILLAGER_ANGRY;
					Location pos = hit.getLocation();
					int num = 10;
					float horOff = 0.15f;
					float verOff = 3f;
					((Player) hit).spawnParticle(par, pos, num, horOff, verOff, horOff);
				}
			}
		}
	}
	
	// Remove taser
	@EventHandler
	public void onPlayerFish(PlayerFishEvent event) {
		if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
			Entity c = event.getCaught();
			if (c instanceof Player) {
				
				// Store the object pending of removal so iteration doesn't break
				TaserInteraction removeTI = null;
				for (TaserInteraction ti : interactions) {
					if (ti.getVictim().equals(c)) {
						removeTI = ti;
						ti.RemoveTase();
					}
				}
				if (removeTI != null) {
					interactions.remove(removeTI);
				}
			}
		}
	}
	
	
	
	// Bug preventing
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		TaserInteraction removeTI = null;
		//event.getPlayer().sendMessage("te moviste wey");
		for (TaserInteraction ti : interactions) {
			if (ti.getVictim().equals(event.getPlayer())) {
				event.setTo(event.getFrom());
				if (event.getFrom().distance(ti.getAttacker().getLocation()) > 25) {
					removeTI = ti;
					ti.RemoveTase();
				}
			}
		}
		if (removeTI != null) {
			interactions.remove(removeTI);
		}
	}
	
	// Cancel taser if player quits
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (TaserInteraction ti : interactions) {
			if (event.getPlayer().equals(ti.getAttacker()) || event.getPlayer().equals(ti.getVictim())) {
				ti.RemoveTase();
				interactions.remove(ti);
			}
		}
	}
	
	// Cancel taser drop if using it
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		for (TaserInteraction ti : interactions) {
			if (event.getPlayer().equals(ti.getAttacker())) {
				if (main.isTaser(event.getItemDrop().getItemStack())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	// Cancel moving taser if using it
	@EventHandler
	public void onItemClick(InventoryClickEvent event) {
		for (TaserInteraction ti : interactions) {
			if (event.getWhoClicked().equals(ti.getAttacker())) {
				if (main.isTaser(event.getCurrentItem())) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	// Cancel switching item if using taser
	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent event) {
		for (TaserInteraction ti : interactions) {
			if (event.getPlayer().equals(ti.getAttacker())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	// Remove taser if one of the players die
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		// Store the object pending of removal so iteration doesn't break
		TaserInteraction removeTI = null;
		Player dead = event.getEntity();
		for (TaserInteraction ti : interactions) {
			if (ti.getVictim().equals(dead) || ti.getAttacker().equals(dead)) {
				removeTI = ti;
				ti.RemoveTase();
			}
		}
		if (removeTI != null) {
			interactions.remove(removeTI);
		}
	}
}
