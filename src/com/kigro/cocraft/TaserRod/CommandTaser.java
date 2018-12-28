package com.kigro.cocraft.TaserRod;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandTaser implements CommandExecutor{

	TaserRod main = JavaPlugin.getPlugin(TaserRod.class);
	
	List<TaserInteraction> interactions = main.interactions;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			// Give it to player
			if (sender instanceof Player) {
				main.CreateTaser();
				((Player) sender).getInventory().addItem(main.taserItem);
				return true;
			}
		}
		if(args.length == 1) {
			if (args[0].equals("bug")) {
				for (TaserInteraction i : interactions) {
					i.RemoveTase();
				}
				interactions.clear();
				return true;
			}
		}
		if(args.length == 1) {
			if (args[0].equals("arr")) {
				for (TaserInteraction i : interactions) {
					sender.sendMessage(i.getAttacker().getName() + " to " + i.getVictim().getName());
				}
				return true;
			}
		}
		else if (args.length == 4) {
			if (args[0].contains("batch")) {
				float x, y, z;
				try {
					x = Float.parseFloat(args[1]);
					y = Float.parseFloat(args[2]);
					z = Float.parseFloat(args[3]);
					
					return true;
				}
				catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}
}
