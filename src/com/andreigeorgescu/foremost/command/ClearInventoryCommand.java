package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventoryCommand implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch(args.length) {
            case 0:
                if(sender.hasPermission("foremost.ci")) {
                	((Player) sender).getInventory().clear();
                    sender.sendMessage(ChatColor.GREEN + "Your inventory has been cleared.");
                } else {
                	sender.sendMessage(Utilities.NO_PERMISSION);
                }
                break;
            case 1:
                if(sender.hasPermission("foremost.ci.others")) {
                	if(sender.getName().equalsIgnoreCase(args[0])) {
                        ((Player) sender).getInventory().clear();
                        sender.sendMessage(ChatColor.GREEN + "Your inventory has been cleared.");
                    } else {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        if(target != null) {
                            target.getInventory().clear();
                            target.sendMessage(ChatColor.GREEN + "Your inventory has been cleared by " + sender.getName());
                            sender.sendMessage(ChatColor.GREEN + "You successfully cleared " + target.getName() + "'s inventory.");
                        } else {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                        }
                    }
                } else {
                	sender.sendMessage(Utilities.NO_PERMISSION);
                }
                break;
            default:
                break;
        }
        return true;
    }
}

