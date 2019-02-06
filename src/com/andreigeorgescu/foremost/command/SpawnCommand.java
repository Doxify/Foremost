package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Foremost;

public class SpawnCommand implements CommandExecutor {
	
	Foremost plugin;
	
	public SpawnCommand(Foremost p) {
		plugin = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.spawn")) {
				if(plugin.config.getSpawn() != null) {
					((Player) sender).teleport(plugin.config.getSpawn());
					sender.sendMessage(ChatColor.GREEN + "Teleported to spawn.");
				} else {
					sender.sendMessage(ChatColor.RED + "Spawn has not been set.");
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
