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
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Saphron Help");
            sender.sendMessage(ChatColor.WHITE + " Website: " + ChatColor.RED + "www.sahpron.org");
            sender.sendMessage(ChatColor.WHITE + " Store: " + ChatColor.RED + "www.saphron.org/store" + ChatColor.WHITE + " or " + ChatColor.RED +  "/buy");
            sender.sendMessage(ChatColor.WHITE + " Discord: " + ChatColor.RED + "www.sahpron.org/discord" + ChatColor.WHITE + ", authenticate with " + ChatColor.RED + "/saphcord");
            sender.sendMessage(ChatColor.GREEN + "General Commands:");
            sender.sendMessage(ChatColor.WHITE + " Teleport to spawn with " + ChatColor.RED + "/spawn");
            sender.sendMessage(ChatColor.WHITE + " Message Players with " + ChatColor.RED + "/msg <player name> <message>" + ChatColor.WHITE + ", reply with " + ChatColor.RED + "/r <message>" );
            sender.sendMessage(ChatColor.WHITE + " Ignore messages from others with " + ChatColor.RED + "/ignore <player name>");
        }

        return true;
    }

}
