package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.andreigeorgescu.foremost.ChatManager;

public class ChatManagerCommand implements CommandExecutor {
	
	private ChatManager chatManager;
	public ChatManagerCommand(ChatManager cm) {
		chatManager = cm;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("foremost.chat")) {
		    switch(args.length) {
                case 1:
                    switch(args[0].toLowerCase()) {
                        case "mute":
                            if(sender.hasPermission("foremost.chat.mute")) {
                                if(chatManager.muteChat()) {
                                    Bukkit.broadcastMessage(ChatColor.RED + "Chat has been muted by " + sender.getName() + ".");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Chat is already muted.");
                                }
                            } else {
                                sender.sendMessage(Utilities.NO_PERMISSION);
                            }
                            break;
                        case "unmute":
                            if(sender.hasPermission("foremost.chat.unmute")) {
                                if(chatManager.unmuteChat()) {
                                    Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been unmuted by " + sender.getName() + "!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Chat is already unmuted.");
                                }
                            } else {
                                sender.sendMessage(Utilities.NO_PERMISSION);
                            }
                            break;
                        case "clear":
                            if(sender.hasPermission("foremost.chat.clear")) {
                                chatManager.clearChat(sender.getName());
                            } else {
                                sender.sendMessage(Utilities.NO_PERMISSION);
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "Usage: /chat <mute, unmute, clear>");
                            break;
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Usage: /chat <mute, unmute, clear>");
                    break;
            }
            return true;
		} else {
			sender.sendMessage(Utilities.NO_PERMISSION);
			return true;
		}
	}

}
