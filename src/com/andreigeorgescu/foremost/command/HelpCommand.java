package com.andreigeorgescu.foremost.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // TODO: Clean up the help command, less links.
        if(sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Skyblock" + ChatColor.RED + " Help");
            sender.sendMessage(ChatColor.GRAY + " Website: " + ChatColor.RED + "www.sahpron.org");
            sender.sendMessage(ChatColor.GRAY + " Store: " + ChatColor.RED + "www.saphron.org/store" + ChatColor.GRAY + " or " + ChatColor.RED +  "/buy");
            sender.sendMessage(ChatColor.GRAY + " Discord: " + ChatColor.RED + "www.sahpron.org/discord" + ChatColor.GRAY + ", authenticate with " + ChatColor.RED + "/saphcord");
            sender.sendMessage(ChatColor.GRAY + "General Commands:");
            sender.sendMessage(ChatColor.GRAY + " Teleport to spawn with " + ChatColor.RED + "/spawn");
            sender.sendMessage(ChatColor.GRAY + " Message Players with " + ChatColor.RED + "/msg <player name> <message>" + ChatColor.GRAY + ", reply with " + ChatColor.RED + "/r <message>" );
            sender.sendMessage(ChatColor.GRAY + " Ignore messages from others with " + ChatColor.RED + "/ignore <player name>");
            sender.sendMessage(ChatColor.GRAY + " For all Island commands use:" + ChatColor.RED + "/is help");
        }

        return true;
    }

}