package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.tphere")) {
            	if(args.length == 1) {
            		Player target = Bukkit.getServer().getPlayer(args[0]);
            		if(target != null) {
            			target.teleport(((Player) sender).getLocation());
            			sender.sendMessage(ChatColor.GREEN + "You have teleported " + target.getName() + " to you.");
            			target.sendMessage(ChatColor.GREEN + sender.getName() + " has teleported you to their location.");
            		} else {
            			sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
            		}
            	} else {
            		sender.sendMessage(ChatColor.RED + "Usage: /tphere <playername>");
            	}
            } else {
            	sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }
        return true;
    }
}
