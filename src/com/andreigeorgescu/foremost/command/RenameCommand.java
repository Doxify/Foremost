package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if(sender.hasPermission("foremost.rename")) {
                if(args.length > 0) {
                    ItemStack itemInHand = ((Player) sender).getItemInHand();
                    if(itemInHand.getType() != Material.AIR) {
                        StringBuilder itemName = new StringBuilder();
                        for (int i = 0; i < args.length; i++) {
                            itemName.append(args[i]);
                            if(i+1 != args.length) {
                                itemName.append(" ");
                            }
                        }

                        String newItemName = ChatColor.translateAlternateColorCodes('&', itemName.toString());
                        ItemMeta itemInHandItemMeta = itemInHand.getItemMeta();
                        itemInHandItemMeta.setDisplayName(newItemName);
                        itemInHand.setItemMeta(itemInHandItemMeta);

                        sender.sendMessage(ChatColor.GREEN + "The item in your hand has been renamed: " + ChatColor.translateAlternateColorCodes('&', itemName.toString()));
                    } else {
                        sender.sendMessage(ChatColor.RED + "You are not holding an item in your hand!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /rename <new name here>");
                }
            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }

        return true;
    }
}
