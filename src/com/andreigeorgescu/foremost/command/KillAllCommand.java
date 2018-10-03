package com.andreigeorgescu.foremost.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

import org.bukkit.ChatColor;

public class KillAllCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("foremost.killall")) {
			int counter = 0;
			for(Entity e : ((Player) sender).getWorld().getEntities()) {
				if(e.getType() == EntityType.BLAZE || e.getType() == EntityType.CREEPER || e.getType() == EntityType.ENDERMAN || e.getType() == EntityType.GHAST || e.getType() == EntityType.MAGMA_CUBE || e.getType() == EntityType.PIG_ZOMBIE || e.getType() == EntityType.SILVERFISH || e.getType() == EntityType.SKELETON || e.getType() == EntityType.SLIME || e.getType() == EntityType.SPIDER || e.getType() == EntityType.ZOMBIE ) {
					e.remove();
					counter++;
				}
			}
			
			sender.sendMessage(ChatColor.GREEN + "Successfully killed " + counter + " harmful mobs.");
		} else {
			sender.sendMessage(Utilities.NO_PERMISSION);
		}
		return true;
	}

}
