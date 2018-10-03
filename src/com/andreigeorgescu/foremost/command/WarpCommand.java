package com.andreigeorgescu.foremost.command;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Utilities;

public class WarpCommand implements CommandExecutor {
	
	Foremost plugin;
	
	public WarpCommand(Foremost p) {
		plugin = p;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.warp")) {
				// Warp List
				// Gets a hashmap of the warps and adds them to a stringBuilder that's sent to the player.
				if(args.length >= 1) {
					if(args[0].equalsIgnoreCase("list")) {
						if(sender.hasPermission("foremost.warp.list")) {
							StringBuilder warpList = new StringBuilder();
							for (Entry<String, Location> item : plugin.config.getWarps().entrySet()) {
							    String name = item.getKey();
							    warpList.append(name + " ");
							}
							
							if(warpList.length() == 0) {
								sender.sendMessage(ChatColor.RED + "There are no warps.");
							} else {
								sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Warps");
								sender.sendMessage(ChatColor.GREEN + "" + warpList);
							}
						} else {
							sender.sendMessage(Utilities.NO_PERMISSION);
						}
							
				
					// Create a warp
					// Validates the command then creates a warp via Config.class
					} else if(args[0].equalsIgnoreCase("create")){
						if(sender.hasPermission("foremost.warp.create")) {
							if(args.length == 2) {
								String warpName = args[1].toLowerCase();
								if(!plugin.config.checkIfWarpExists(warpName)) {
									Location warpLocation = ((Player) sender).getLocation();
									plugin.config.addWarp(warpName, warpLocation);
									sender.sendMessage(ChatColor.GREEN + warpName + " has been created.");
								} else {
									sender.sendMessage(ChatColor.RED + "A warp named " + warpName + " already exists.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "Usage: /warp create <warp name>");
							}
						} else {
							sender.sendMessage(Utilities.NO_PERMISSION);
						}
					
					// Delete a warp
					// Validates the command then deletes the warp via Config.class
					} else if(args[0].equalsIgnoreCase("delete")) {
						if(sender.hasPermission("foremost.warp.del")) {
							if(args.length == 2) {
								String warpName = args[1].toLowerCase();
								if(plugin.config.checkIfWarpExists(warpName)) {
									if(plugin.config.deleteWarp(warpName)) {
										sender.sendMessage(ChatColor.GREEN + "Successfully deleted " + warpName + " warp.");
									} else {
										sender.sendMessage(ChatColor.RED + "A warp named " + warpName + " doesn't exist.");
									}
								} else {
									sender.sendMessage(ChatColor.RED + "A warp named " + warpName + " doesn't exist.");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "Usage: /warp delete <warp name>");
							}
						} else {
							sender.sendMessage(Utilities.NO_PERMISSION);
						}
					} else {
						// HANDLE WARPS HERE
						if(args.length == 1) {
							String warpName = args[0].toLowerCase();
							if(plugin.config.checkIfWarpExists(warpName)) {
								Location warpLocation = plugin.config.getWarp(warpName);
								((Player) sender).teleport(warpLocation);
								sender.sendMessage(ChatColor.GREEN + "You have been teleported to " + warpName + ".");
							} else {
								sender.sendMessage(ChatColor.RED + "A warp named " + warpName + " doesn't exist.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Usage: /warp <warp name, list, create, delete>");
						}
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /warp <warp name, list, create, delete>");
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
