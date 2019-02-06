package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.day")) {
            	((Player) sender).getWorld().setTime(1000);
                sender.sendMessage(ChatColor.GREEN + "Time has been set to day.");
            } else {
            	sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }
        return true;
    }
}
