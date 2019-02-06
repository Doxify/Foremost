package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(label.equalsIgnoreCase("gmc")) {
                if(args.length == 1) {
                    if(sender.hasPermission("foremost.gamemode.others")) {
                    	Player target = Bukkit.getServer().getPlayer(args[0]);
                        if(target != null) {
                            target.setGameMode(GameMode.CREATIVE);
                            target.sendMessage(ChatColor.GREEN + "Your gamemode has been set to creative by " + sender.getName() + ".");
                            sender.sendMessage(ChatColor.GREEN + "Successfully changed " + target.getName() + "'s gamemode to creative.");
                        } else {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                        }
                    } else {
                    	sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                } else {
                    if(sender.hasPermission("foremost.gamemode")) {
                    	((Player) sender).setGameMode(GameMode.CREATIVE);
                        sender.sendMessage(ChatColor.GREEN + "Successfully changed your gamemode to creative.");
                    } else {
                    	sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                }
            } else if(label.equalsIgnoreCase("gms")) {
                if(args.length == 1) {
                    if(sender.hasPermission("foremost.gamemode.others")) {
                    	Player target = Bukkit.getServer().getPlayer(args[0]);
                        if(target != null) {
                            target.setGameMode(GameMode.SURVIVAL);
                            target.sendMessage(ChatColor.GREEN + "Your gamemode has been set to survival by " + sender.getName() + ".");
                            sender.sendMessage(ChatColor.GREEN + "Successfully changed " + target.getName() + "'s gamemode to survival.");
                        } else {
                            sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                        }
                    } else {
                    	sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                } else {
                    if(sender.hasPermission("foremost.gamemode")) {
                    	((Player) sender).setGameMode(GameMode.SURVIVAL);
                        sender.sendMessage(ChatColor.GREEN + "Successfully changed your gamemode to survival.");
                    } else {
                    	sender.sendMessage(Utilities.NO_PERMISSION);
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /<gmc or gms>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can only run this command in-game.");
        }

        return true;
    }
}
