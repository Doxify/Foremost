package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import com.andreigeorgescu.foremost.Foremost;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairCommand implements CommandExecutor {

    private Foremost plugin;
    private Player commandSender;

    public RepairCommand(Foremost p) {
        plugin = p;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            commandSender = (Player) sender;
            String senderUUID = ((Player) sender).getUniqueId().toString();
            switch (args.length) {
                // Sender ran "/repair"
                case 0: {
                    if (sender.hasPermission("foremost.repair.cooldown.5")) {
                        if (!plugin.cooldownManager.hasCooldown(senderUUID, "REPAIR")) {
                            ItemStack item = ((Player) sender).getInventory().getItemInHand();
                            if(repairItem(item)) {
                                sender.sendMessage(ChatColor.GREEN + "Successfully repaired the item in your hand.");
                                sender.sendMessage(ChatColor.GRAY + "/repair is now on cooldown for 5 minutes.");
                                plugin.cooldownManager.addCooldown(senderUUID, "REPAIR", 300);
                            } else {
                                sender.sendMessage(ChatColor.RED + "The item in your hand cannot be repaired, please try another item.");
                            }
                            return true;
                        } else {
                            // TODO: Add proper time format here: 00:00
                            commandSender.sendMessage(ChatColor.RED + "/repair is on cooldown for " + plugin.cooldownManager.getRemainingCooldown(senderUUID, "REPAIR") + " more seconds!");
                            commandSender.sendMessage(ChatColor.GRAY + "Purchase Diamond rank to remove all command cooldowns! /buy");
                            return true;
                        }
                    } else if (sender.hasPermission("foremost.repair.bypass")) {
                        ItemStack item = ((Player) sender).getInventory().getItemInHand();
                        if(repairItem(item)) {
                            sender.sendMessage(ChatColor.GREEN + "Successfully repaired the item in your hand.");
                            plugin.cooldownManager.addCooldown(senderUUID, "REPAIR", 300);
                        } else {
                            sender.sendMessage(ChatColor.RED + "The item in your hand cannot be repaired, please try another item.");
                        }
                        return true;
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                        return true;
                    }
                }
                // Sender ran "/repair <args>"
                case 1: {
                    if (sender.hasPermission("foremost.repair.all.cooldown.60")) {
                        if (args[0].equalsIgnoreCase("all")) {
                            if (!plugin.cooldownManager.hasCooldown(senderUUID, "REPAIRALL")) {

                                int repairedItems = 0;

                                // Repairing all items in the players inventory
                                for(ItemStack item : ((Player) sender).getInventory().getContents()) {
                                    if(repairItem(item)) {
                                        repairedItems++;
                                    }
                                }

                                // Repairing all items in the players armor inventory
                                for(ItemStack item : ((Player) sender).getInventory().getArmorContents()) {
                                    if(repairItem(item)) {
                                        repairedItems++;
                                    }
                                }

                                if (repairedItems != 0) {
                                    sender.sendMessage(ChatColor.GREEN + "Successfully repaired " + repairedItems + " item(s).");
                                    plugin.cooldownManager.addCooldown(senderUUID, "REPAIRALL", 3600);
                                } else {
                                    sender.sendMessage(ChatColor.RED + "There are no items that can be repaired in your inventory.");
                                }

                                return true;
                            } else {
                                // TODO: Add proper time format here: 00:00
                                commandSender.sendMessage(ChatColor.RED + "/repair all is on cooldown for " + plugin.cooldownManager.getRemainingCooldown(senderUUID, "REPAIRALL") + " more seconds!");
                                commandSender.sendMessage(ChatColor.GRAY + "Purchase Diamond rank to remove all command cooldowns! /buy");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /repair all");
                            return true;
                        }
                    } else if(sender.hasPermission("foremost.repair.bypass")) {
                        int repairedItems = 0;
                        for (ItemStack item : ((Player) sender).getInventory()) {
                            if (repairItem(item)) {
                                repairedItems++;
                            }
                        }
                        if (repairedItems != 0) {
                            sender.sendMessage(ChatColor.GREEN + "Successfully repaired " + repairedItems + " item(s).");
                        } else {
                            sender.sendMessage(ChatColor.RED + "There are no items that can be repaired in your inventory.");
                        }
                        return true;
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                        return true;
                    }
                }
                default: {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean repairItem(ItemStack item) {
       if(item != null && item.getType() != Material.AIR) {
           if(!item.getItemMeta().spigot().isUnbreakable()) {
               if (item.getDurability() != 0) {
                   item.setDurability((short) (item.getDurability() - item.getType().getMaxDurability()));
                   return true;
               } else {
                   return false;
               }
           } else {
               return false;
           }
       } else {
           return false;
       }
    }
}
