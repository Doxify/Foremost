package com.andreigeorgescu.foremost.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.andreigeorgescu.foremost.ChatManager;
import com.andreigeorgescu.foremost.Utilities;

public class ChatManagerCommand implements CommandExecutor {
	
	private ChatManager chatManager;
	public ChatManagerCommand(ChatManager cm) {
		chatManager = cm;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("mute")) {
				if(sender.hasPermission("foremost.chat.mute")) {
					chatManager.muteChat(sender.getName());
				} else {
					sender.sendMessage(Utilities.NO_PERMISSION);
				}
			} else if(args[0].equalsIgnoreCase("unmute")) {
				if(sender.hasPermission("foremost.chat.unmute")) {
					chatManager.unmuteChat(sender.getName());
				} else {
					sender.sendMessage(Utilities.NO_PERMISSION);
				}
			} else if(args[0].equalsIgnoreCase("clear")) {
				if(sender.hasPermission("foremost.chat.clear")) {
					chatManager.clearChat(sender.getName());
				} else {
					sender.sendMessage(Utilities.NO_PERMISSION);
				}
 			} else {
				sender.sendMessage(ChatColor.RED + "Usage: /chat <mute, unmute, clear>");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: /chat <mute, unmute, clear>");
		}
		
		return true;
	}

}
