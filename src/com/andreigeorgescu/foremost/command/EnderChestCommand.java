package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if (sender.hasPermission("foremost.enderchest")) {
                ((Player) sender).openInventory(((Player) sender).getEnderChest());
                sender.sendMessage(ChatColor.GREEN + "Opening your enderchest.");
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
