package com.andreigeorgescu.foremost.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.andreigeorgescu.foremost.Utilities;

public class RepairCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.repair")) {
            	ItemStack item = ((Player) sender).getInventory().getItemInHand();
                if(item.getType().getMaxDurability() > 0) {
                    // Item can be repaired,
                    if(item.getDurability() != item.getType().getMaxDurability()) {
                        item.setDurability((short)(item.getDurability() - item.getType().getMaxDurability()));
                        sender.sendMessage(ChatColor.GREEN + "The item in your hand has been repaired.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "The item in your hand already has max durability.");
                    }
                } else {
                    // Item cannot be repaired.
                    sender.sendMessage(ChatColor.RED + "This item cannot be repaired.");
                }
            } else {
            	sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }
        return true;
    }
}
