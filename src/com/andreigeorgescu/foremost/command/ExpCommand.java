package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ExpCommand implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("foremost.exp")) {
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("give")) {
					try {
						Player target = Bukkit.getServer().getPlayer(args[1]);
						target.setExp(10);
					} catch (NumberFormatException error) {
						sender.sendMessage(ChatColor.RED + "Usage: /exp give <playername> <ammount>");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /exp give <playername> <ammount>");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Usage: /exp give <playername> <ammount>");
			}
		} else {
			sender.sendMessage(Utilities.NO_PERMISSION);
		}
		return true;
	}

}
