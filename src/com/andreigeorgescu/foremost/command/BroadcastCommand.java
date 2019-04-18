package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import de.Herbystar.TTA.TTA_Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("foremost.broadcast")) {
            StringBuilder broadcastMessage = new StringBuilder();
            String lastChatColor = ChatColor.WHITE.toString();

            for(String arg : args) {
                if(arg.contains("&")) {
                    String formatted = ChatColor.translateAlternateColorCodes('&', arg);
                    lastChatColor = ChatColor.getLastColors(formatted);
                    broadcastMessage.append(formatted + " ");
                } else {
                    broadcastMessage.append(lastChatColor + arg + " ");
                }
            }

            Bukkit.broadcastMessage(broadcastMessage.toString());


            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                TTA_Methods.sendActionBar(p, broadcastMessage.toString(), 180);
            }

            return true;
        } else {
            sender.sendMessage(Utilities.NO_PERMISSION);
            return true;
        }
    }
}
