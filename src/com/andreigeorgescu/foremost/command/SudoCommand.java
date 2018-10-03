package com.andreigeorgescu.foremost.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

public class SudoCommand implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("foremost.sudo")) {
			if(args.length > 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if(target != null) {
					StringBuilder command = new StringBuilder();
					for(int i = 1; i < args.length; i++) {
						command.append(args[i] + " ");
					}
					target.performCommand(command.toString());
					sender.sendMessage(ChatColor.GREEN + "Successfully made " + target.getName() + " run " + ChatColor.GRAY + "/" + command.toString());
					target.sendMessage(ChatColor.GREEN + sender.getName() + " made you run " + ChatColor.GRAY + "/" + command.toString());
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Usage: /sudo <playername> <command>");
				return true;
			}
		} else {
			sender.sendMessage(Utilities.NO_PERMISSION);
			return true;
		}
    }
}
