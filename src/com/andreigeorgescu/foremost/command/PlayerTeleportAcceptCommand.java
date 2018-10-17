package com.andreigeorgescu.foremost.command;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.Profile;
import com.andreigeorgescu.foremost.Utilities;
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

public class PlayerTeleportAcceptCommand implements CommandExecutor {

    Foremost plugin;

    public PlayerTeleportAcceptCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.teleport.accept")) {
                switch(label.toUpperCase()) {
                    case "TPA": {
                        if(args.length == 1) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if(target != null) {
                                Profile targetProfile = plugin.profileManager.getProfile(target.getUniqueId().toString());
                                if(target.getName() != sender.getName()) {
                                    targetProfile.setTeleportRequest(sender.getName());
                                    TextComponent teleportAcceptMessage = new TextComponent("");
                                        TextComponent acceptMessage = new TextComponent("Accept " + sender.getName() + "'s request: ");
                                        TextComponent denyMessage = new TextComponent("Deny " + sender.getName() + "'s request: ");
                                        teleportAcceptMessage.setColor(ChatColor.GOLD);
                                        teleportAcceptMessage.addExtra(sender.getName() + " has requested to teleport to you.");
                                        acceptMessage.setColor(ChatColor.GREEN);
                                        acceptMessage.addExtra("[Accept]");
                                        denyMessage.setColor(ChatColor.RED);
                                        denyMessage.addExtra("[Deny]");

                                        acceptMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Accept " + sender.getName() + "'s teleport request.").create()));
                                        acceptMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
                                        denyMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "Deny " + sender.getName() + "'s teleport request.").create()));
                                        denyMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));

                                        target.spigot().sendMessage(teleportAcceptMessage);
                                        target.spigot().sendMessage(acceptMessage);
                                        target.spigot().sendMessage(denyMessage);
                                        sender.sendMessage(ChatColor.GREEN + "Successfully sent " + target.getName() + " a teleport request." );
                                        return true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You cannot send a tpa request to yourself!");
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
                                    targetProfile.setTeleportRequest(sender.getName());
                                    TextComponent teleportAcceptMessage = new TextComponent("");
                                    TextComponent acceptMessage = new TextComponent("Accept " + sender.getName() + "'s request: ");
                                    TextComponent denyMessage = new TextComponent("Deny " + sender.getName() + "'s request: ");
                                    teleportAcceptMessage.setColor(ChatColor.GOLD);
                                    teleportAcceptMessage.addExtra(sender.getName() + " has requested you to teleport to them.");
                                    acceptMessage.setColor(ChatColor.GREEN);
                                    acceptMessage.addExtra("[Accept]");
                                    denyMessage.setColor(ChatColor.RED);
                                    denyMessage.addExtra("[Deny]");

                                    acceptMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Accept " + sender.getName() + "'s teleport request.").create()));
                                    acceptMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahereaccept"));
                                    denyMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "Deny " + sender.getName() + "'s teleport request.").create()));
                                    denyMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));

                                    target.spigot().sendMessage(teleportAcceptMessage);
                                    target.spigot().sendMessage(acceptMessage);
                                    target.spigot().sendMessage(denyMessage);
                                    sender.sendMessage(ChatColor.GREEN + "Successfully sent " + target.getName() + " a tpahere request." );
                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "You cannot send a tpahere request to yourself!");
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
                            sender.sendMessage(ChatColor.GREEN + sender.getName() + " has teleported to you!");
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
                            sender.sendMessage(ChatColor.RED + "You don't have a pending teleport request.");
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
