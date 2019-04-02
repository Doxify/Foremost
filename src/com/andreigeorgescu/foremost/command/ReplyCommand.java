package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import com.saphron.nsa.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import com.saphron.nsa.NSA;

public class ReplyCommand implements CommandExecutor {
	
    private final Foremost plugin;
    private final NSA nsaPlugin;

    public ReplyCommand(Foremost plugin, NSA nsaPlugin) {
        this.plugin = plugin;
        this.nsaPlugin = nsaPlugin;
    }
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.msg")) {
				Profile profile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
				if(args.length >= 1) {
					if(profile.getLastReply() != null) {
						Player target = Bukkit.getServer().getPlayer(profile.getLastReply());
						if(target != null) {
							User targetUser = nsaPlugin.userManager.getUser(target.getUniqueId().toString());
							if(targetUser.getTogglePM() == false || sender.hasPermission("nsa.togglepm.bypass")) {
								StringBuilder message = new StringBuilder();
								for(int i = 0; i < args.length; i++) {
									message.append(args[i] + " ");
								}
								
	                    		// Checking if the target has the sender ignored.
								// TODO: Implement ignore checks here
								if(true) {
									if(true) {
//	                    		if(!nsaPlugin.getProfileManager().getProfile(target.getUniqueId().toString()).isIgnored(((Player) sender).getUniqueId().toString()) || sender.hasPermission("nsa.ignore.bypass")) {
//	                    			if(!nsaPlugin.getProfileManager().getProfile(((Player) sender).getUniqueId().toString()).isIgnored(target.getUniqueId().toString())) {
	                    				// The target is not on the ignored list of the sender or the sender has the bypass permission, sending the message.
	                    				if(!plugin.messageManager.sendMessage((Player) sender, target, message.toString())) {
	                    					sender.sendMessage(ChatColor.RED + "There was an error sending your message. Please contact staff.");
	                    					return true;
	                    				}
	                    			} else {
	                    				sender.sendMessage(ChatColor.RED + "You can't message someone on your ignored list.");
	                    				return true;
	                    			}
	                    		} else {
	                    			sender.sendMessage(ChatColor.LIGHT_PURPLE + "(To " + target.getName() + ") " + message);
                    				return true;
	                    		}
	                    		
							} else {
		                		sender.sendMessage(ChatColor.RED + target.getName() + " has their messages toggled off.");
                				return true;
							}
		                 
						} else {
							sender.sendMessage(ChatColor.RED + profile.getLastReply() + " is not online.");
            				return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You have not messaged anyone recently.");
        				return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /r <message>");
    				return true;
				}
			} else {
				sender.sendMessage(Utilities.NO_PERMISSION);
				return true;
			}
		} else {
			sender.sendMessage("You can only use this command in-game.");
			return true;
		}
		return true;
	}
}
