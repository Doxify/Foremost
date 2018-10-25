package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.fly")) {
               if(((Player) sender).isFlying()) {
                   ((Player) sender).setAllowFlight(false);
                   ((Player) sender).setFlying(false);
                   sender.sendMessage(ChatColor.RED + "Flight has been disabled.");
                   return true;
               } else {
                   ((Player) sender).setAllowFlight(true);
                   ((Player) sender).setFlying(true);
                   sender.sendMessage(ChatColor.GREEN + "Flight has been enabled.");
                   return true;
               }
            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
                return true;
            }
        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
            return true;
        }
    }
}
