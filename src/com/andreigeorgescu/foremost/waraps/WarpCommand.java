package com.andreigeorgescu.foremost.waraps;

import com.andreigeorgescu.foremost.Foremost;
import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class WarpCommand implements CommandExecutor {

    private WarpManager warpManager;

    public WarpCommand(WarpManager wm) {
        this.warpManager = wm;
    }

    // List Warps (Open GUI) "foremost.warp"
    // Create Warp "foremost.warp.create"
    // Delete Warp "foremost.warp.delete"

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("foremost.warp")) {
                switch(args.length) {
                    // /warp create <warp-name>
                    // /warp delete <warp-name>
                    case 2: {
                        if(p.hasPermission("foremost.warp.create") || p.hasPermission("foremost.warp.delete")) {
                            switch(args[0].toUpperCase()) {
                                case "CREATE": {
                                    if(!p.hasPermission("foremost.warp.create")) {
                                        p.sendMessage(Utilities.NO_PERMISSION);
                                        return true;
                                    }

                                    String warpName = args[1];
                                    Location warpLocation = p.getLocation();
                                    if(warpManager.addWarp(warpName, warpLocation)) {
                                        p.sendMessage(ChatColor.GREEN + "Successfully created warp: " + warpName);
                                        break;
                                    } else {
                                        p.sendMessage(ChatColor.RED + "A warp with the name '" + warpName + "' already exists.");
                                        break;
                                    }
                                }
                                case "DELETE": {
                                    if(!p.hasPermission("foremost.warp.delete")) {
                                        p.sendMessage(Utilities.NO_PERMISSION);
                                        return true;
                                    }

                                    String warpName = args[1];
                                    if(warpManager.removeWarp(warpName)) {
                                        p.sendMessage(ChatColor.GREEN + "Successfully deleted warp: " + warpName);
                                        break;
                                    } else {
                                        p.sendMessage(ChatColor.RED + "A warp with the name '" + warpName + "' doesn't exist.");
                                        break;
                                    }
                                }
                                default: {
                                    p.sendMessage(ChatColor.RED + "Usage: ");
                                    p.sendMessage(ChatColor.RED + "/warp create <warp-name>");
                                    p.sendMessage(ChatColor.RED + "/warp delete <warp-name>");
                                    break;
                                }
                            }
                            return true;
                        } else {
                            p.sendMessage(Utilities.NO_PERMISSION);
                            return true;
                        }
                    }
                    // /warp
                    case 0: {
                        Bukkit.getScheduler().runTaskAsynchronously(Foremost.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Inventory warpInventory = warpManager.getWarpMenu(p);
                                    p.openInventory(warpInventory);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    p.sendMessage(ChatColor.RED + "You don't have any available warps.");
                                }
                            }
                        });
                        break;
                    }
                    default: {
                        p.sendMessage(ChatColor.RED + "Usage: ");
                        p.sendMessage(ChatColor.RED + "/warp");
                        break;
                    }
                }
                return true;
            } else {
                p.sendMessage(Utilities.NO_PERMISSION);
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Warps cannot be used from console.");
            return true;
        }
    }
}
