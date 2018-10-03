package com.andreigeorgescu.foremost.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

public class TeleportAllCommand implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.tpall")) {
            	int counter = 0;
            	for(World w : Bukkit.getServer().getWorlds()) {
            		for(Entity e : w.getEntities()) {
            			if(e instanceof Player) {
            				if(((Player) e).getPlayer() != sender) {
            					e.teleport(((Player) sender).getLocation());
                				((Player) e).sendMessage(ChatColor.GREEN + sender.getName() + " has teleported you to their location.");
            					counter++;
            				}
    					}
            		}
            	}            	
            	sender.sendMessage(ChatColor.GREEN + "Successfully teleported " + counter + " players to your location.");
            } else {
            	sender.sendMessage(Utilities.NO_PERMISSION);
            }
        } else {
        	sender.sendMessage(Utilities.NON_PLAYER_SENDER);
        }
        return true;
    }
}
