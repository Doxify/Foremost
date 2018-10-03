package com.andreigeorgescu.foremost.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.andreigeorgescu.foremost.Utilities;

public class NightCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.night")) {
            	((Player) sender).getWorld().setTime(13000);
                sender.sendMessage(ChatColor.GREEN + "Time has been set to night.");
            } else {
            	sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }
        return true;
    }

}
