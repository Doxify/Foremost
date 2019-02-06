package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportPositionCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.tppos")) {
				if(args.length == 3) {
					try {
						double x = Double.parseDouble(args[0]);
						double y = Double.parseDouble(args[1]);
						double z = Double.parseDouble(args[2]);
						((Player) sender).teleport(new Location(((Player) sender).getWorld(), x, y, z));
						sender.sendMessage(ChatColor.GREEN + "You have been teleported to " + x + ", " + y + ", " + z + ".");
					} catch(NumberFormatException error) {
						sender.sendMessage(ChatColor.RED + "Usage: /tppos <x> <y> <z>");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /tppos <x> <y> <z>");
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
