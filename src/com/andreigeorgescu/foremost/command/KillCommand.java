package com.andreigeorgescu.foremost.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

public class KillCommand implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("foremost.kill")) {
			if(args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if(target != null) {
					target.setHealth(0);
					target.sendMessage(ChatColor.RED + "You have been killed by " + sender.getName() + ".");
					sender.sendMessage(ChatColor.GREEN + "You have killed "  + args[0] + ".");
				} else {
					sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Usage: /kill <playername>");
			}
		} else {
			sender.sendMessage(Utilities.NO_PERMISSION);
		}
		return true;
	}
}
