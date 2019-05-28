package com.andreigeorgescu.foremost.kits.commands;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.kits.Kit;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.andreigeorgescu.foremost.utils.NotificationHologram;
import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class KitAdminCommand implements CommandExecutor {

    Foremost plugin;
    KitsManager kitsManager;

    public KitAdminCommand(Foremost p) { plugin = p; kitsManager = plugin.getKitsManager(); }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(Bukkit.getPluginManager().isPluginEnabled("Saphub")) {
            sender.sendMessage(ChatColor.RED + "/kit is disabled on this server.");
            return true;
        }

        if(sender.hasPermission("foremost.kitAdmin")) {
            /*
            Commands:
                1. /kitAdmin add "name" "cooldown (seconds)"
                2. /kitAdmin delete "name"
                3. /kitAdmin give "player" "name"
                4. /kitAdmin setDefault "name"
             */

            switch (args.length) {
                case 3: {
                    if(args[0].equalsIgnoreCase("ADD")) {
                        if(sender instanceof Player) {
                            Player p = (Player) sender;
                            try {
                                String kitName = args[1];
                                int cooldown = Integer.parseInt(args[2]);
                                ItemStack[] kitItems = p.getInventory().getContents();

                                if (kitsManager.addKit(kitName, cooldown, kitItems)) {
                                    sender.sendMessage(ChatColor.GREEN + "Successfully added kit: " + kitName);
                                    break;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "A kit with that name already exists.");
                                    break;
                                }

                            } catch (ClassCastException e) {
                                sender.sendMessage(getKitAdminCommandGuide());
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
                                givePlayerKit(target, targetKit);
                                target.sendMessage(ChatColor.GREEN + "You've been given the " + targetKit.getName() + " kit!");
                                break;
                            } else {
                                sender.sendMessage(ChatColor.RED + args[1] + " is not online.");
                                break;
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Couldn't find a kit with the name " + args[2]);
                            break;
                        }
                    } else {
                        sender.sendMessage(getKitAdminCommandGuide());
                        break;
                    }
                }
                case 2: {
                    if(args[0].equalsIgnoreCase("DELETE")) {
                        String kitName = args[1];

                        if (kitsManager.deleteKit(kitName)) {
                            sender.sendMessage(ChatColor.GREEN + "Successfully deleted kit: " + kitName);
                            break;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Couldn't find a kit with the name: " + kitName);
                            break;
                        }
                    } else if(args[0].equalsIgnoreCase("SETDEFAULT")) {
                        String kitName = args[1];

                        if(kitsManager.kitExists(kitName)) {
                            plugin.config.setDefaultKit(kitName);
                            sender.sendMessage(ChatColor.GREEN + "Successfully set default kit to: " + kitName);
                            break;
                        } else {
                            sender.sendMessage(ChatColor.RED + kitName + " does not exist.");
                            break;
                        }
                    } else {
                        sender.sendMessage(getKitAdminCommandGuide());
                        break;
                    }
                }
                default: {
                    sender.sendMessage(getKitAdminCommandGuide());
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
        string.append("\n/kitAdmin setDefault <name>");

        return string.toString();
    }

    private void givePlayerKit(Player p, Kit kit) {
        ItemStack kitItems[] = kit.getKit().clone();
        HashMap<Integer, ItemStack> items = p.getInventory().addItem(kitItems);

        if (items.size() > 0) {
            for (ItemStack item : items.values()) {
                p.getWorld().dropItem(p.getLocation(), item);
            }
        }

        p.updateInventory();

        NotificationHologram notificationHologram = new NotificationHologram();
        notificationHologram.setPlayer(p)
                .addLine(ChatColor.LIGHT_PURPLE + "Kit Received")
                .addLine(ChatColor.GREEN + ChatColor.BOLD.toString() + kit.getName())
                .build();
    }

}
