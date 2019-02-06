package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import com.andreigeorgescu.foremost.Foremost;
import com.saphron.nsa.NSA;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

    private final Foremost plugin;
    private final NSA nsaPlugin;

    public MessageCommand(Foremost plugin, NSA nsaPlugin) {
        this.plugin = plugin;
        this.nsaPlugin = nsaPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
        	String prefix = ChatColor.translateAlternateColorCodes('&', plugin.nsaPlugin.getChat().getPlayerPrefix((Player) sender));
            if(sender.hasPermission("foremost.msg")) {
            	if(args.length <= 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
                } else {
                    Player target = Bukkit.getServer().getPlayer(args[0]);                    
                    
                    StringBuilder message = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        message.append(args[i] + " ");
                    }
                    if(target != null) {
                    	if((boolean) nsaPlugin.getProfileManager().getProfile(target.getUniqueId().toString()).getTogglePM() == false || sender.hasPermission("nsa.togglepm.bypass")) {
                    		// Checking if the target has the sender ignored.
                    		if(!nsaPlugin.getProfileManager().getProfile(target.getUniqueId().toString()).isIgnored(((Player) sender).getUniqueId().toString()) || sender.hasPermission("nsa.ignore.bypass") ) {
                    			if(!nsaPlugin.getProfileManager().getProfile(((Player) sender).getUniqueId().toString()).isIgnored(target.getUniqueId().toString())) {
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
                    			// The sender IS on the ignored list, sending a fake message.
                    			sender.sendMessage(ChatColor.LIGHT_PURPLE + "(To " + prefix + target.getName() + ChatColor.LIGHT_PURPLE + ") " + message);
            					return true;
                    		}
                    		
                    	} else {
                    		sender.sendMessage(ChatColor.RED + target.getName() + " has their messages toggled off.");
        					return true;
                    	}
                    } else {
                    	sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
    					return true;
                    }
                }
            } else {
            	sender.sendMessage(Utilities.NO_PERMISSION);
				return true;
            }

        }
        return true;
    }

	public Foremost getPlugin() {
		return plugin;
	}
}
