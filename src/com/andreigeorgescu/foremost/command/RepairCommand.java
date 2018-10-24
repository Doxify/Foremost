package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.andreigeorgescu.foremost.Utilities;
import org.bukkit.inventory.meta.Repairable;

import java.util.ArrayList;
import java.util.List;

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
            Profile profile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
            switch (args.length) {
                // Sender ran "/repair"
                case 0: {
                    if (sender.hasPermission("foremost.repair.cooldown.5")) {
                        if (plugin.cooldownManager.getRepairCooldown(senderUUID, 300) == -1 || plugin.cooldownManager.getRepairCooldown(senderUUID, 300) == 0) {
                            ItemStack item = ((Player) sender).getInventory().getItemInHand();
                            if(repairItem(item)) {
                                sender.sendMessage(ChatColor.GREEN + "Successfully repaired the item in your hand.");
                                sender.sendMessage(ChatColor.GRAY + "/repair is now on cooldown for 5 minutes.");
                                plugin.cooldownManager.setRepairCooldown(senderUUID);
                            } else {
                                sender.sendMessage(ChatColor.RED + "The item in your hand cannot be repaired, please try another item.");
                            }
                            return true;
                        } else {
                            // TODO: Add proper time format here: 00:00
                            commandSender.sendMessage(ChatColor.RED + "/repair is on cooldown for " + plugin.cooldownManager.getRepairCooldown(senderUUID, 300) + " more seconds!");
                            commandSender.sendMessage(ChatColor.GRAY + "Purchase Diamond rank to remove all command cooldowns! /buy");
                            return true;
                        }
                    } else if (sender.hasPermission("foremost.repair.bypass")) {
                        ItemStack item = ((Player) sender).getInventory().getItemInHand();
                        if(repairItem(item)) {
                            sender.sendMessage(ChatColor.GREEN + "Successfully repaired the item in your hand.");
                            plugin.cooldownManager.setRepairCooldown(senderUUID);
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
                            if (plugin.cooldownManager.getRepairAllCooldown(senderUUID, 3600) == -1 || plugin.cooldownManager.getRepairAllCooldown(senderUUID, 3600) == 0) {
                                int repairedItems = repairAll((Player) sender);
                                if (repairedItems != 0) {
                                    sender.sendMessage(ChatColor.GREEN + "Successfully repaired " + repairedItems + " item(s).");
                                    plugin.cooldownManager.setRepairCooldown(senderUUID);
                                } else {
                                    sender.sendMessage(ChatColor.RED + "There are no items that can be repaired in your inventory.");
                                }

                                return true;
                            } else {
                                // TODO: Add proper time format here: 00:00
                                commandSender.sendMessage(ChatColor.RED + "/repair all is on cooldown for " + plugin.cooldownManager.getRepairAllCooldown(senderUUID, 300) + " more seconds!");
                                commandSender.sendMessage(ChatColor.GRAY + "Purchase Diamond rank to remove all command cooldowns! /buy");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /repair all");
                            return true;
                        }
//                    } else if(sender.hasPermission("foremost.repair.bypass")) {
//                        int repairedItems = 0;
//                        for (ItemStack item : ((Player) sender).getInventory()) {
//                            if (repairItem(item)) {
//                                repairedItems++;
//                            }
//                        }
//                        if (repairedItems != 0) {
//                            sender.sendMessage(ChatColor.GREEN + "Successfully repaired " + repairedItems + " item(s).");
//                            plugin.cooldownManager.setRepairCooldown(senderUUID);
//                        } else {
//                            sender.sendMessage(ChatColor.RED + "There are no items that can be repaired in your inventory.");
//                        }
//                        return true;
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
        if (item.getType().getMaxDurability() > 0) {
            // Item can be repaired,
            if (item.getDurability() != item.getType().getMaxDurability()) {
                item.setDurability((short) (item.getDurability() - item.getType().getMaxDurability()));
//                commandSender.sendMessage(ChatColor.GREEN + "The item in your hand has been repaired.");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int repairAll(Player p) {
        int repairedItems = 0;
        for(ItemStack item : p.getInventory().getContents()) {
            if(item != null) {
                if (item.getDurability() > item.getType().getMaxDurability()) {
                    item.setDurability((short)0);
                    repairedItems++;
                }
            }
        }
//        for(ItemStack item : p.getEquipment().getArmorContents()) {
//            if(item != null) {
//                if (item.getDurability() > item.getType().getMaxDurability()) {
//                    item.setDurability((short)0);
//                    repairedItems++;
//                }
//            }
//        }
        return repairedItems;
    }
}
