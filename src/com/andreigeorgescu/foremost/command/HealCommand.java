package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

public class HealCommand implements CommandExecutor {

    private Foremost plugin;

    public HealCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String senderUUID = ((Player) sender).getUniqueId().toString();
            Profile profile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
            switch (args.length) {
                // Sender ran "/heal"
                case 0: {
                    if(sender.hasPermission("foremost.heal.cooldown.3")) {
                        if(!plugin.cooldownManager.hasCooldown(senderUUID, "HEAL")) {
                            healPlayer(p);
                            sender.sendMessage(ChatColor.GRAY + "/heal is now on cooldown for 3 minutes.");
                            plugin.cooldownManager.addCooldown(senderUUID, "HEAL", 180);
                        } else {
                            // TODO: Add proper time format here: 00:00
                            if(plugin.cooldownManager.hasCooldownExpired(senderUUID, "HEAL")) {
                                plugin.cooldownManager.deleteCooldown(senderUUID, "HEAL");
                                healPlayer(p);
                                sender.sendMessage(ChatColor.GRAY + "/heal is now on cooldown for 3 minutes.");
                                plugin.cooldownManager.addCooldown(senderUUID, "HEAL", 180);
                            } else {
                                sender.sendMessage(ChatColor.RED + "/heal is on cooldown for " + plugin.cooldownManager.getRemainingCooldown(senderUUID, "HEAL") + " more seconds!");
                                sender.sendMessage(ChatColor.GRAY + "Purchase Diamond rank to remove all command cooldowns! /buy");
                            }
                        }
                    } else if (p.hasPermission("foremost.heal.bypass")) {
                        healPlayer(p);
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                    break;
                }
                // Sender ran "/heal <playername>"
                case 1: {
                    if(sender.hasPermission("foremost.heal.others")) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if(target != null) {
                            healPlayer(p);
                            sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                            target.sendMessage(ChatColor.GREEN + "You have been healed by " + sender.getName() + ".");
                        } else {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                        }
                    } else {
                        sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                    break;
                }
                default: {
                    p.sendMessage(ChatColor.RED + "Usage: /heal");
                    break;
                }
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You can only use this command in-game.");
            return true;
        }
    }

    public void healPlayer(Player commandSender) {
        commandSender.setHealth(commandSender.getHealthScale());
        commandSender.setFoodLevel(20);
        commandSender.sendMessage(ChatColor.GREEN + "You have been healed.");
    }
}
