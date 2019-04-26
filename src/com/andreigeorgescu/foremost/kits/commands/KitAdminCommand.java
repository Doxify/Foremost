package com.andreigeorgescu.foremost.kits.commands;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.kits.Kit;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitAdminCommand implements CommandExecutor {

    Foremost plugin;
    KitsManager kitsManager;

    public KitAdminCommand(Foremost p) { plugin = p; kitsManager = plugin.getKitsManager(); }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Bukkit.getPluginManager().getPlugin("Saphub").isEnabled()) {
            sender.sendMessage(ChatColor.RED + "/kit is disabled on this server.");
            return true;
        }

        if(sender.hasPermission("foremost.kitAdmin")) {
            Player p = (Player) sender;

            /*
            Commands:
                1. /kitAdmin add "name" "cooldown (seconds)"
                2. /kitAdmin delete "name"
                3. /kitAdmin give "player" "name"
             */

            switch (args.length) {
                case 3: {
                    if(args[0].equalsIgnoreCase("ADD")) {
                        if(sender instanceof Player) {
                            try {
                                String kitName = args[1];
                                int cooldown = Integer.parseInt(args[2]);
                                ItemStack[] kitItems = p.getInventory().getContents();

                                if (kitsManager.addKit(kitName, cooldown, kitItems)) {
                                    p.sendMessage(ChatColor.GREEN + "Successfully added kit: " + kitName);
                                    break;
                                } else {
                                    p.sendMessage(ChatColor.RED + "A kit with that name already exists.");
                                    break;
                                }

                            } catch (ClassCastException e) {
                                p.sendMessage(getKitAdminCommandGuide());
                                break;
                            }
                        } else {
                            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
                        }
                    } else if(args[0].equalsIgnoreCase("GIVE")) {
                        if(kitsManager.kitExists(args[2])) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if(target != null) {
                                Kit targetKit = kitsManager.getKitObject(args[2]);
                                kitsManager.givePlayerKit(target, targetKit);
                                target.sendMessage(ChatColor.GREEN + "You've been given the " + targetKit.getName() + " kit!");
                                break;
                            } else {
                                p.sendMessage(ChatColor.RED + args[1] + " is not online.");
                                break;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Couldn't find a kit with the name " + args[2]);
                            break;
                        }
                    } else {
                        p.sendMessage(getKitAdminCommandGuide());
                        break;
                    }
                }
                case 2: {
                    if(args[0].equalsIgnoreCase("DELETE")) {
                        String kitName = args[1];

                        if (kitsManager.deleteKit(kitName)) {
                            p.sendMessage(ChatColor.GREEN + "Successfully deleted kit: " + kitName);
                            break;
                        } else {
                            p.sendMessage(ChatColor.RED + "Couldn't find a kit with the name: " + kitName);
                            break;
                        }
                    } else {
                        p.sendMessage(getKitAdminCommandGuide());
                        break;
                    }
                }
                default: {
                    p.sendMessage(getKitAdminCommandGuide());
                    break;
                }
            }
        } else {
            sender.sendMessage(Utilities.NO_PERMISSION);
        }
        return true;
    }

    public String getKitAdminCommandGuide() {
        StringBuilder string = new StringBuilder(ChatColor.RED + "Usage: ");
        string.append("\n/kitAdmin add <name> <cooldown in seconds>");
        string.append("\n/kitAdmin delete <name>");
        string.append("\n/kitAdmin give <player> <name>");

        return string.toString();
    }

}
