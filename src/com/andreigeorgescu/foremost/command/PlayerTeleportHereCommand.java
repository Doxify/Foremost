package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTeleportHereCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.teleport.here")) {

            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }
        return true;
    }
}
