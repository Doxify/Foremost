package com.andreigeorgescu.foremost.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;


public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.speed")) {
            	if(args.length == 1) {
                    try {
                        int speed = Integer.valueOf(args[0]);
                        if(speed > 0 && speed < 11) {
                            if(((Player) sender).isFlying()) {
                                ((Player) sender).setFlySpeed((float) Integer.parseInt(args[0]) / 10);
                                sender.sendMessage(ChatColor.GREEN + "Set flight speed to " + args[0] + ".");
                            } else {
                                ((Player) sender).setWalkSpeed((float) Integer.parseInt(args[0]) / 10);
                                sender.sendMessage(ChatColor.GREEN + "Set walking speed to " + args[0] + ".");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /speed <1-10>");
                        }
                    } catch(NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Usage: /speed <1-10>");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /speed <1-10>");
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
