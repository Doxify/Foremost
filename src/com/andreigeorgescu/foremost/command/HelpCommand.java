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
            sender.sendMessage(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Saphron Help");
            sender.sendMessage(ChatColor.WHITE + " Website: " + ChatColor.DARK_AQUA + ChatColor.UNDERLINE + "www.sahpron.org");
            sender.sendMessage(ChatColor.WHITE + " Store: " + ChatColor.DARK_AQUA + ChatColor.UNDERLINE + "www.saphron.org/store" + ChatColor.WHITE + " or " + ChatColor.DARK_AQUA +  "/buy");
            sender.sendMessage(ChatColor.WHITE + " Discord: " + ChatColor.DARK_AQUA + ChatColor.UNDERLINE + "www.sahpron.org/discord" + ChatColor.WHITE + ", authenticate with " + ChatColor.DARK_AQUA + "/saphcord");
            sender.sendMessage(ChatColor.WHITE + "General Commands:");
            sender.sendMessage(ChatColor.WHITE + " Teleport to spawn with " + ChatColor.DARK_AQUA + "/spawn");
            sender.sendMessage(ChatColor.WHITE + " Message Players with " + ChatColor.DARK_AQUA + "/msg <player name> <message>" + ChatColor.WHITE + ", reply with " + ChatColor.DARK_AQUA + "/r <message>" );
            sender.sendMessage(ChatColor.WHITE + " Ignore messages from others with " + ChatColor.DARK_AQUA + "/ignore <player name>");
        }

        return true;
    }
}
