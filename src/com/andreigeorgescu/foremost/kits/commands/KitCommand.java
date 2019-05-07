package com.andreigeorgescu.foremost.kits.commands;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.kits.Kit;
import com.andreigeorgescu.foremost.kits.KitGUI;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.andreigeorgescu.foremost.utils.NotificationHologram;
import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor {

    Foremost plugin;
    KitsManager kitsManager;

    public KitCommand(Foremost p) { plugin = p; kitsManager = plugin.getKitsManager(); }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(plugin.nsaPlugin.isHub()) {
            sender.sendMessage(ChatColor.RED + "This feature is disabled in the lobby.");
            return true;
        }

        if(sender instanceof Player) {
            Player p = (Player) sender;

            /*
            Commands:
                1. /kit - Sends the player a list of the kits *** NEW FEATURE: Now opens interface.
                2. /kit "name" - Gives the player the requested kit
                3. /kit preview "name" - Gives the player a preview gui
                4. /kit list - Gives a list of kits to the player (admin cmd)
             */
            switch (args.length) {
                case 0: {
                    p.openInventory(KitGUI.getKitsInterface(p));
                    break;
                }
                case 1: {
                    String kitName = args[0];
                    if(kitName.equalsIgnoreCase("list")) {
                        kitsManager.getKitList(p);
                        break;
                    } else if(kitsManager.kitExists(kitName)) {
                        Kit kit = kitsManager.getKitObject(kitName);

                        if(kit.hasPermission(p) && kit.getCooldown() != -1) {
                            if (!kitsManager.hasCooldown(p.getUniqueId(), kit.getName())) {
                                kitsManager.givePlayerKit(p, kit);
                                sendNotificationHologram(p, kit);
                                break;
                            } else {
                                if (kitsManager.isCooldownOver(p.getUniqueId(), kit.getName())) {
                                    kitsManager.removeCooldown(p.getUniqueId(), kit.getName());
                                    kitsManager.givePlayerKit(p, kit);
                                    sendNotificationHologram(p, kit);
                                    break;
                                } else {
                                    LocalDateTime cooldown = kitsManager.getCooldown(p.getUniqueId(), kit.getName());
                                    p.sendMessage(ChatColor.RED + kit.getName() + " is on cooldown for " + kitsManager.getCooldownString(cooldown) + ".");
                                    break;
                                }
                            }
                        } else if(kit.getCooldown() == -1) {
                            if(p.hasPermission("foremost.kitAdmin")) {
                                kitsManager.givePlayerKit(p, kit);
                                sendNotificationHologram(p, kit);
                                break;
                            } else {
                                p.sendMessage(Utilities.NO_PERMISSION);
                                break;
                            }
                        } else {
                            p.sendMessage(Utilities.noKitPermission(kit.getName()));
                            break;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "That kit doesn't exist.");
                        break;
                    }
                }
                case 2: {
                    if(args[0].equalsIgnoreCase("PREVIEW")) {
                        String kitName = args[1];
                        if(kitsManager.kitExists(kitName)) {
                            Kit kit = kitsManager.getKitObject(kitName);
                            p.openInventory(new KitGUI().generateKitPreviewGUI(p, kit));
                            break;
                        } else {
                            p.sendMessage(ChatColor.RED + "That kit doesn't exist.");
                            break;
                        }
                    } else {
                        p.sendMessage(getKitCommandGuide());
                        break;
                    }
                }
                default: {
                    p.sendMessage(getKitCommandGuide());
                    break;
                }
            }

        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
        }

        return true;
    }

    public String getKitCommandGuide() {
        StringBuilder string = new StringBuilder(ChatColor.RED + "Usage: ");
        string.append("\n/kit - View available kits");
        string.append("\n/kit list - Get a list of kits");
        string.append("\n/kit <name> - Redeem a kit");
        string.append("\n/kit preview <name> - Preview a kit");

        return string.toString();
    }

    public void sendNotificationHologram(Player p, Kit kit) {
        List<String> lines = new ArrayList<>();

        if(plugin.kitsManager.hasCooldown(p.getUniqueId(), kit.getName())) {
            LocalDateTime cooldown = kitsManager.getCooldown(p.getUniqueId(), kit.getName());
            lines.add(ChatColor.LIGHT_PURPLE + "Kit Redeemed: ");
            lines.add(ChatColor.GREEN + ChatColor.BOLD.toString() + kit.getName() + ChatColor.RESET);
            lines.add(ChatColor.WHITE + "This kit is on cooldown");
            lines.add(ChatColor.WHITE + "for " + ChatColor.RED + ChatColor.BOLD.toString() + kitsManager.getCooldownString(cooldown) + ChatColor.RESET);
        } else {
            lines.add(ChatColor.LIGHT_PURPLE + "Kit Redeemed:");
            lines.add(ChatColor.GREEN + ChatColor.BOLD.toString() + kit.getName() + ChatColor.RESET);
        }

        // Hologram notification
        NotificationHologram notificationHologram = new NotificationHologram();
        notificationHologram
                .setPlayer(p)
                .setType(NotificationHologram.TYPE.KIT)
                .setLines(lines)
                .build();
    }
}
