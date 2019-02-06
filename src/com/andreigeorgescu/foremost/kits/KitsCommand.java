package com.andreigeorgescu.foremost.kits;

import com.andreigeorgescu.foremost.Foremost;
import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitsCommand implements CommandExecutor {

    Foremost plugin;

    public KitsCommand(Foremost p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            switch(args.length) {
                // User ran /kit
                case 0: {
                    p.sendMessage(plugin.kitManager.getKitList(p));
                    break;
                }
                // User ran /kit <arg>
                // Possible args: kitName
                case 1: {
                    String kitName = args[0];
                    Kit kit = plugin.kitManager.getKitObject(kitName);
                    if(kit instanceof Kit) {
                        if(kit.hasPermission(p)) {
                            kit.giveKit(p);
                            p.sendMessage(ChatColor.GREEN + "Successfully redeemed kit: " + kitName);
                            break;
                        } else {
                            p.sendMessage(Utilities.noKitPermission(kitName));
                            break;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "That kit does not exist.");
                        break;
                    }
                }
                // User ran /kit <arg1> <arg2>
                // Possible arg1: delete
                // Possible arg2: kitName
                case 2: {
                    if(p.hasPermission("foremost.admin")) {
                        String kitName = args[1];
                        if(args[0].equalsIgnoreCase("delete")) {
                            if(plugin.kitManager.deleteKit(kitName)) {
                                p.sendMessage(ChatColor.GREEN + "Successfully deleted kit: " + kitName);
                                break;
                            } else {
                                p.sendMessage(ChatColor.RED + "Could not find and delete kit: " + kitName);
                                break;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Usage: /kit delete <kit name>");
                            break;
                        }
                    } else {
                        p.sendMessage(Utilities.NO_PERMISSION);
                        break;
                    }
                }
                // User ran /kit <arg1> <arg2> <arg3>
                // Possible arg1: set
                // Possible arg2: kitName (String)
                // Possible arg3: cooldown (int)
                case 3: {
                    if(p.hasPermission("foremost.admin")) {
                        if(args[0].equalsIgnoreCase("set")) {
                            try {
                                String kitName = args[1];
                                int cooldown = Integer.parseInt(args[2]);

                                if(plugin.kitManager.addKit(kitName, p.getInventory().getContents(), cooldown)) {
                                    p.sendMessage(ChatColor.GREEN + "Successfully added kit: " + kitName);
                                    break;
                                } else {
                                    p.sendMessage(ChatColor.RED + "A kit with that name has already been added. Please try again.");
                                    break;
                                }
                            } catch (NullPointerException | ClassCastException e) {
                                p.sendMessage(ChatColor.RED + "Usage: /kit set <kit name> <cooldown secs.>");
                                e.printStackTrace();
                                break;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Usage: /kit set <kit name> <cooldown secs.>");
                            break;
                        }
                    } else {
                        p.sendMessage(Utilities.NO_PERMISSION);
                        break;
                    }
                }
                default: {
                    sender.sendMessage(ChatColor.RED + "Usage: /kit <kitname> - Get a kit");
                    sender.sendMessage(ChatColor.RED + "Usage: /kit - List available kits");
                    break;
                }
            }
        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
        }
        return true;
    }
}
