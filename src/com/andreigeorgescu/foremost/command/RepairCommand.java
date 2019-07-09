package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.cooldowns.Cooldown;
import com.andreigeorgescu.foremost.cooldowns.CooldownManager;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Foremost.getPlugin().nsaPlugin.isHub()) {
            sender.sendMessage(ChatColor.RED + "This feature is disabled in the lobby.");
            return true;
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;
            switch (args.length) {
                // Sender ran "/repair"
                case 0: {
                    if (sender.hasPermission("foremost.repair.cooldown.5")) {
                        if(!Foremost.getPlugin().cooldownManager.hasCooldown(CooldownManager.COOLDOWN.REPAIR, p.getUniqueId())) {
                            ItemStack item = p.getItemInHand();
                            if(repairItem(item)) {
                                p.sendMessage(ChatColor.GREEN + "Successfully repaired the item in your hand.");
                                p.sendMessage(ChatColor.GRAY + "/repair is now on cooldown for 5 minutes.");
                                Foremost.getPlugin().cooldownManager.addCooldown(CooldownManager.COOLDOWN.REPAIR, p.getUniqueId(), 300);
                            } else {
                                p.sendMessage(ChatColor.RED + "The item in your hand cannot be repaired.");
                            }
                        } else {
                            Cooldown cooldown = Foremost.getPlugin().cooldownManager.getCooldown(CooldownManager.COOLDOWN.REPAIR, p.getUniqueId());
                            p.sendMessage(ChatColor.RED + "/repair is on cooldown for " + Utilities.getTimeStringWordsWithSeconds(cooldown.getRemainingCooldown()));
                            p.sendMessage(ChatColor.RED + "Purchase a higher rank for a reduced or no cooldown.");
                        }
                    } else if (sender.hasPermission("foremost.repair.bypass")) {
                        ItemStack item = p.getItemInHand();
                        if(repairItem(item)) {
                            p.sendMessage(ChatColor.GREEN + "Successfully repaired the item in your hand.");
                        } else {
                            p.sendMessage(ChatColor.RED + "The item in your hand cannot be repaired.");
                        }
                    } else {
                        p.sendMessage(Utilities.NO_PERMISSION);
                    }
                    return true;
                }
                // Sender ran "/repair <args>"
                case 1: {
                    if (sender.hasPermission("foremost.repair.all.cooldown.60")) {
                        if (args[0].equalsIgnoreCase("all")) {
                            if(!Foremost.getPlugin().cooldownManager.hasCooldown(CooldownManager.COOLDOWN.REPAIRALL, p.getUniqueId())) {
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
                                    p.sendMessage(ChatColor.GREEN + "Successfully repaired " + repairedItems + " item(s).");
                                    p.sendMessage(ChatColor.GRAY + "/repair is on cooldown for 60 seconds.");
                                    Foremost.getPlugin().cooldownManager.addCooldown(CooldownManager.COOLDOWN.REPAIRALL, p.getUniqueId(), 60);
                                } else {
                                    p.sendMessage(ChatColor.RED + "There are no items that can be repaired in your inventory.");
                                }
                            } else {
                                Cooldown cooldown = Foremost.getPlugin().cooldownManager.getCooldown(CooldownManager.COOLDOWN.REPAIRALL, p.getUniqueId());
                                p.sendMessage(ChatColor.RED + "/repair all is on cooldown for " + Utilities.getTimeStringWordsWithSeconds(cooldown.getRemainingCooldown()));
                                p.sendMessage(ChatColor.RED + "Purchase a higher rank for a reduced or no cooldown.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /repair all");
                        }
                    } else if(sender.hasPermission("foremost.repair.bypass")) {
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
                        } else {
                            sender.sendMessage(ChatColor.RED + "There are no items that can be repaired in your inventory.");
                        }
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                    return true;
                }
                default: {
                    return true;
                }
            }
        }
        return true;
    }

    public static boolean repairItem(ItemStack item) {
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
