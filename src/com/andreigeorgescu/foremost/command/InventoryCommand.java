package com.andreigeorgescu.foremost.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.andreigeorgescu.foremost.Utilities;

public class InventoryCommand implements CommandExecutor {
	
//	p.openInventory(clickedPlayer.getInventory);

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("foremost.inv")) {
				if(args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null) {
						if(!sender.hasPermission("foremost.inv.bypass")) {
							Inventory fauxInventory = Bukkit.createInventory(null, 54, "Inventory Viewer: " + target.getName());
							for(ItemStack item : target.getInventory().getContents()) {
								if(item != null) {
									fauxInventory.addItem(item);
								}
							}
							((Player) sender).openInventory(fauxInventory);
							sender.sendMessage(ChatColor.GREEN + "Now viewing " + target.getName() + "'s inventory.");
						} else {
							((Player) sender).openInventory(target.getInventory());
							sender.sendMessage(ChatColor.GREEN + "Now opening " + target.getName() + "'s inventory.");
						}
					} else {
						sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /inv <playername>");
				}
			} else {
				sender.sendMessage(Utilities.NO_PERMISSION);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You can only use this command in-game.");
		}
		return true;
	}
}
