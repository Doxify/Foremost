package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Foremost;

import net.md_5.bungee.api.ChatColor;

public class BackCommand implements CommandExecutor {
	
	Foremost plugin;
	
	public BackCommand(Foremost p) {
		plugin = p;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.back")) {
				Location lastLocation = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString()).getLastLocation();
				if(lastLocation != null) {
					((Player) sender).teleport(lastLocation);
					sender.sendMessage(ChatColor.GREEN + "You've been teleported to the last location you were teleported to.");
				} else {
					sender.sendMessage(ChatColor.RED + "Couldn't find a previous location you were teleported to.");
				}
			} else {
				sender.sendMessage(Utilities.NO_PERMISSION);
			}
		} else {
			sender.sendMessage(Utilities.NON_PLAYER_SENDER);
		}
		return true;
	}
}
