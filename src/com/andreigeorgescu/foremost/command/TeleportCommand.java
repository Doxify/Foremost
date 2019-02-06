package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.teleport")) {
                switch(args.length) {
                    case 1:
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if(target != null) {
                            ((Player) sender).teleport(target.getLocation());
                            sender.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName());
                            break;
                        } else {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                            break;
                        }
                    default:
                        sender.sendMessage(ChatColor.RED + "Usage: /tp <playername>");
                        break;
                }
                return true;

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
