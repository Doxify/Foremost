package com.andreigeorgescu.foremost.staff.commands;

import com.andreigeorgescu.foremost.Foremost;
import com.saphron.nsa.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

        if(p.hasPermission("foremost.staff")) {
            if(Foremost.getPlugin().staffManager.isStaffMode(p)) {
                // The user is already in staff mode, they want to leave staff mode.
                Foremost.getPlugin().staffManager.removeFromStaffMode(p);
                return true;
            } else {
                // The user is not in staff mode, they want to enter staff mode.
                Foremost.getPlugin().staffManager.addToStaffMode(p);
                return true;
            }
        } else {
            p.sendMessage(Utilities.NO_PERMISSION);
            return true;
        }

        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
        }


        return true;
    }

}
