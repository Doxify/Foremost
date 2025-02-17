package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTeleportCommand implements CommandExecutor {

    Foremost plugin;

    public PlayerTeleportCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(plugin.nsaPlugin.isHub()) {
            sender.sendMessage(ChatColor.RED + "This feature is disabled in the lobby.");
            return true;
        }

        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.teleport.accept")) {
                switch(label.toUpperCase()) {
                    case "TPA": {
                        if(args.length == 1) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if(target != null) {
                                Profile targetProfile = plugin.profileManager.getProfile(target.getUniqueId().toString());
                                if(target.getName() != sender.getName()) {
                                    if(targetProfile.getTeleportRequest() != sender.getName()) {
                                        targetProfile.setTeleportRequest(sender.getName());
                                        new FancyMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "INCOMING TELEPORT REQUEST")
                                                .then("\n" + ChatColor.GRAY + "From: " + ChatColor.YELLOW + sender.getName())
                                                .then("\n" + ChatColor.GRAY + "Type: " + ChatColor.YELLOW + "They teleport to you")
                                                .then("\n" + ChatColor.DARK_GREEN + "Click ")
                                                .then(ChatColor.GREEN + ChatColor.BOLD.toString() + "ACCEPT")
                                                    .tooltip(ChatColor.GREEN + "Accept " + sender.getName() + "'s request")
                                                    .command("/tpaccept")
                                                .then(ChatColor.DARK_GREEN + " to accept " + sender.getName() + "'s request.")

//                                                .then(ChatColor.GRAY + " or ")
//                                                .then(ChatColor.RED + ChatColor.BOLD.toString() + "DENY")
//                                                    .tooltip(ChatColor.RED + "Deny " + sender.getName() + "'s request")
//                                                    .command("/tpdeny")
                                            .send(target);
                                        sender.sendMessage(ChatColor.GREEN + "Successfully sent " + target.getName() + " a teleport request." );
                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You've already sent a tpa request to " + target.getName() + ".");
                                        return true;
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You cannot send a request to yourself.");
                                    return true;
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /tpa <playername>");
                            return true;
                        }

                    }
                    case "TPAHERE": {
                        if(args.length == 1) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if(target != null) {
                                Profile targetProfile = plugin.profileManager.getProfile(target.getUniqueId().toString());
                                if(target.getName() != sender.getName()) {
                                    if(targetProfile.getTeleportRequest() != sender.getName()) {
                                        targetProfile.setTeleportRequest(sender.getName());
                                        new FancyMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "INCOMING TELEPORT REQUEST")
                                                .then("\n" + ChatColor.GRAY + "From: " + ChatColor.YELLOW + sender.getName())
                                                .then("\n" + ChatColor.GRAY + "Type: " + ChatColor.YELLOW + "You teleport to them")
                                                .then("\n" + ChatColor.DARK_GREEN + "Click ")
                                                .then(ChatColor.GREEN + ChatColor.BOLD.toString() + "ACCEPT")
                                                    .tooltip(ChatColor.GREEN + "Accept " + sender.getName() + "'s request")
                                                    .command("/tpahereaccept")
                                                .then(ChatColor.DARK_GREEN + " to accept " + sender.getName() + "'s request.")

//                                                .then(ChatColor.GRAY + " or ")
//                                                .then(ChatColor.RED + ChatColor.BOLD.toString() + "DENY")
//                                                    .tooltip(ChatColor.RED + "Deny " + sender.getName() + "'s request")
//                                                    .command("/tpdeny")
                                            .send(target);
                                        sender.sendMessage(ChatColor.GREEN + "Successfully sent " + target.getName() + " a tpahere request." );
                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You've already sent a tpahere request to " + target.getName() + ".");
                                        return true;
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You cannot send a request to yourself.");
                                    return true;
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /tpahere <playername>");
                            return true;
                        }

                    }
                    case "TPACCEPT": {
                        Profile profile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
                        String targetName = profile.getTeleportRequest();
                        if(targetName != null ) {
                            Player targetPlayer = (Player) Bukkit.getPlayer(profile.getTeleportRequest());
                            Profile senderProfile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
                            Player target = Bukkit.getPlayer(targetName);

                            ((Player) targetPlayer).teleport(((Player) sender).getLocation());
                            senderProfile.setTeleportRequest(null);
                            target.sendMessage(ChatColor.GREEN + "You've been teleported to " + sender.getName() + "!");
                            sender.sendMessage(ChatColor.GREEN + target.getName() + " has teleported to you!");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                            return true;
                        }
                    }
                    case "TPAHEREACCEPT": {
                        Profile profile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
                        String targetName = profile.getTeleportRequest();
                        if(targetName != null ) {
                            Profile senderProfile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());
                            Player target = Bukkit.getPlayer(targetName);

                            ((Player) sender).teleport(((Player) target).getLocation());
                            senderProfile.setTeleportRequest(null);
                            sender.sendMessage(ChatColor.GREEN + "You've been teleported to " + targetName + "!");
                            target.sendMessage(ChatColor.GREEN + sender.getName() + " has teleported to you!");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                            return true;
                        }
                    }
                    case "TPDENY": {
                        Profile senderProfile = plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString());

                        if(senderProfile.getTeleportRequest() != null) {
                            Player target = Bukkit.getPlayer(senderProfile.getTeleportRequest());
                            senderProfile.setTeleportRequest(null);
                            sender.sendMessage(ChatColor.GREEN + "Successfully denied " + target.getName() + "'s teleport request.");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have a pending teleport request.");
                            return true;
                        }
                    }
                    default:
                        sender.sendMessage(ChatColor.RED + "Usage: /tpa <playername>, /tpaccept, /tpdeny");
                        return true;
                }
            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
            }
        }

    return true;
    }
}
