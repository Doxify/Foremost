package com.andreigeorgescu.foremost.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Utilities;

public class SetSpawnCommand implements CommandExecutor {

	Foremost plugin;
	
	public SetSpawnCommand(Foremost p) {
		plugin = p;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.setspawn")) {
				Location spawnLocation = ((Player) sender).getLocation();
				plugin.config.setSpawn(spawnLocation);
				sender.sendMessage(ChatColor.GREEN + "Successfully set spawn to your location.");
			} else {
				sender.sendMessage(Utilities.NO_PERMISSION);
			}
		} else {
			sender.sendMessage(Utilities.NON_PLAYER_SENDER);
		}
		return true;
	}

}