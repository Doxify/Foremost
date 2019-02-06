package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import com.andreigeorgescu.foremost.Foremost;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    Foremost plugin;

    public StaffModeCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = ((Player) sender);
            if(sender.hasPermission("foremost.staffmode")) {
                if(!plugin.staffModeManager.hasStaffMode(p.getUniqueId().toString())) {
                    plugin.staffModeManager.addStaff(p.getUniqueId().toString());
                    p.sendMessage(ChatColor.GREEN + "Staff mode has been enabled.");
                    return true;
                } else {
                    plugin.staffModeManager.removeStaff(p.getUniqueId().toString());

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
