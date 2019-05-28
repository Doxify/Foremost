package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.MessageManager;
import com.saphron.nsa.Utilities;
import com.andreigeorgescu.foremost.Foremost;
import com.saphron.nsa.NSA;

import com.saphron.nsa.user.User;
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
        	String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix((Player) sender));
            if(sender.hasPermission("foremost.msg")) {
            	if(args.length <= 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
                } else {
            		Player s = (Player) sender; // sender
                    Player t = Bukkit.getServer().getPlayer(args[0]); // target
                    
                    StringBuilder message = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        message.append(args[i] + " ");
                    }

                    if(t != null) {
                    	User senderUser = nsaPlugin.userManager.getUser(s.getUniqueId());
						User targetUser = nsaPlugin.userManager.getUser(t.getUniqueId());
                    	if(targetUser.getTogglePM() == false || sender.hasPermission("nsa.togglepm.bypass")) {
                    		// Checking if the target has the sender ignored.
							if(!targetUser.isInIgnoreList(s.getUniqueId()) || s.hasPermission("nsa.ignore.bypass")) {
								if(!senderUser.isInIgnoreList(t.getUniqueId())) {
                    				// The target is not on the ignored list of the sender or the sender has the bypass permission, sending the message.
                    				if(!plugin.messageManager.sendMessage(s, t, message.toString())) {
                    					s.sendMessage(ChatColor.RED + "There was an error sending your message. Please contact staff.");
                    					return true;
                    				}
                    			} else {
                    				s.sendMessage(ChatColor.RED + "You can't message someone on your ignored list.");
                					return true;
                    			}
                    		} else {
                    			// The sender IS in the ignored list, sending a fake message.
                    			s.sendMessage(ChatColor.LIGHT_PURPLE + "(To " + prefix + t.getName() + ChatColor.LIGHT_PURPLE + ") " + message);
								if(!senderUser.getToggleSounds()) {
									MessageManager.playMessageSound(t);
								}
            					return true;
                    		}
                    		
                    	} else {
                    		s.sendMessage(ChatColor.RED + t.getName() + " has their messages toggled off.");
        					return true;
                    	}
                    } else {
                    	s.sendMessage(ChatColor.RED + args[0] + " is not online.");
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
}
