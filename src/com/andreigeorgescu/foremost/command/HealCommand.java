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
    private Player commandSender;

    public HealCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            commandSender = (Player) sender;
            String senderUUID = ((Player) sender).getUniqueId().toString();
            Profile profile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
            switch (args.length) {
                // Sender ran "/heal"
                case 0: {
                    if(commandSender.hasPermission("foremost.heal.cooldown.3")) {
                        if(plugin.cooldownManager.getHealCooldown(senderUUID, 180 ) == -1 || plugin.cooldownManager.getHealCooldown(senderUUID, 180 ) == 0 ) {
                            healPlayer();
                            commandSender.sendMessage(ChatColor.GRAY + "/heal is now on cooldown for 3 minutes.");
                            plugin.cooldownManager.setHealCooldown(senderUUID);
                        } else {
                            // TODO: Add proper time format here: 00:00
                            commandSender.sendMessage(ChatColor.RED + "/heal is on cooldown for " + plugin.cooldownManager.getHealCooldown(senderUUID, 300) + " more seconds!");
                            commandSender.sendMessage(ChatColor.GRAY + "Purchase Diamond rank to remove all command cooldowns! /buy");
                            return true;
                        }
                    }
                }
                default: {
                    commandSender.sendMessage(ChatColor.RED + "Usage: /heal");
                    return true;
                }


//            if(args.length == 0) {
//                if(sender.hasPermission("foremost.heal")) {
//                	((Player) sender).setHealth(((Player) sender).getHealthScale());
//                    ((Player) sender).setFoodLevel(20);
//                    sender.sendMessage(ChatColor.GREEN + "You have been healed.");
//                } else {
//                	sender.sendMessage(Utilities.NO_PERMISSION);
//                }
//            } else {
//                if(sender.hasPermission("foremost.heal.others")) {
//                	Player target = Bukkit.getServer().getPlayer(args[0]);
//                    if(target != null) {
//                        ((Player) target).setHealth(((Player) target).getHealthScale());
//                        ((Player) target).setFoodLevel(20);
//                        sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
//                        target.sendMessage(ChatColor.GREEN + "You have been healed by " + sender.getName() + ".");
//                    } else {
//                        sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
//                    }
//                } else {
//                	sender.sendMessage(Utilities.NO_PERMISSION);
//                }
//            }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can only use this command in-game.");
            return true;
        }
    }

    public void healPlayer() {
        commandSender.setHealth(commandSender.getHealthScale());
        commandSender.setFoodLevel(20);
        commandSender.sendMessage(ChatColor.GREEN + "You have been healed.");
    }
}
