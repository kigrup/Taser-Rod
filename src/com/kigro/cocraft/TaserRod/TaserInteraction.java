package com.kigro.cocraft.TaserRod;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TaserInteraction {
	
	
	private Entity attacker;
	private Entity victim;
	
	public TaserInteraction(Entity Attacker, Entity Victim) {
		attacker = Attacker;
		victim = Victim;
	}
	
	public Entity getAttacker() {
		return attacker;
	}
	
	public Entity getVictim() {
		return victim;
	}
	
	public void Tase() {
		
		((Player) victim).setWalkSpeed(0);
		((LivingEntity) victim).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999, 200));
	}
	
	public void RemoveTase() {
		
		((Player) victim).setWalkSpeed(0.2f);
		((LivingEntity) victim).removePotionEffect(PotionEffectType.JUMP);
	}
}
