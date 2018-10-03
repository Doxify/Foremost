package com.andreigeorgescu.foremost.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0) {
                if(sender.hasPermission("foremost.heal")) {
                	((Player) sender).setHealth(((Player) sender).getHealthScale());
                    ((Player) sender).setFoodLevel(20);
                    sender.sendMessage(ChatColor.GREEN + "You have been healed.");
                } else {
                	sender.sendMessage(Utilities.NO_PERMISSION);
                }
            } else {
                if(sender.hasPermission("foremost.heal.others")) {
                	Player target = Bukkit.getServer().getPlayer(args[0]);
                    if(target != null) {
                        ((Player) target).setHealth(((Player) target).getHealthScale());
                        ((Player) target).setFoodLevel(20);
                        sender.sendMessage(ChatColor.GREEN + "You have healed " + target.getName() + ".");
                        target.sendMessage(ChatColor.GREEN + "You have been healed by " + sender.getName() + ".");
                    } else {
                        sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                    }
                } else {
                	sender.sendMessage(Utilities.NO_PERMISSION);
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can only use this command in-game.");
        }
        return true;
    }
}
