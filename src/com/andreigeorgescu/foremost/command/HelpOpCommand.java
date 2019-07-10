package com.andreigeorgescu.foremost.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpOpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {
        p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Please use Discord to get immediate help from staff!");
        p.sendMessage(ChatColor.GRAY + " 1. Join our server via wwww.saphron.org/discord");
        p.sendMessage(ChatColor.GRAY + " 2. Navigate to #ticket-create in General");
        p.sendMessage(ChatColor.GRAY + " 3. Create a new ticket with '-new <subject>'");
        p.sendMessage(ChatColor.GRAY + " 4. All staff will be instantly notified via discord :)");
        return true;
    }


}
