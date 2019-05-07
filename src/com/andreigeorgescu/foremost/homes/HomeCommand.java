package com.andreigeorgescu.foremost.homes;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.utils.NotificationHologram;
import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand implements CommandExecutor {

    Foremost plugin;

    public HomeCommand(Foremost plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(plugin.nsaPlugin.isHub()) {
            sender.sendMessage(ChatColor.RED + "This feature is disabled in the lobby.");
            return true;
        }

        if(sender instanceof Player) {
            Player p = (Player) sender;
            int allowedHomes = plugin.homeManager.getAllowedHomes(p);

            switch (args.length) {
                // This just opens an inventory with the player's homes
                // /home
                case 0: {
                    if(allowedHomes != 0) {
                        List<Home> homes = plugin.homeManager.getHomes(p);
                        if(homes != null) {
                            p.openInventory(HomeInterface.getHomeInventory(homes, allowedHomes));
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have any homes.");
                            sendHelpMessage(p);
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You aren't allowed to create homes");
                    }
                    break;
                }
                // Handling removing a home
                // /home set <home name> - sets a home
                // /home remove <home name> - removes a home
                case 2: {
                    if(args[0].equalsIgnoreCase("remove")) {
                        Home home = plugin.homeManager.getHome(p, args[1]);
                        if(home instanceof Home) {
                            if(plugin.homeManager.removeHome(p, args[1])) {
                                NotificationHologram notificationHologram = new NotificationHologram();
                                notificationHologram
                                        .setPlayer(p)
                                        .setHome(home)
                                        .setType(NotificationHologram.TYPE.HOME_DELETE)
                                        .build();
                                p.sendMessage(ChatColor.GREEN + "Successfully removed home: " + args[1]);
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Could not find home named " + args[1]);
                        }
                    } else if(args[0].equalsIgnoreCase("set")) {
                        List<Home> homes = plugin.homeManager.getHomes(p);
                        if(allowedHomes != 0) {
                            if(homes == null || homes.size() < allowedHomes) {
                                Home home = new Home(args[1], p.getLocation(), p.getUniqueId());
                                if(plugin.homeManager.addhome(home)) {
                                    NotificationHologram notificationHologram = new NotificationHologram();
                                    notificationHologram
                                            .setPlayer(p)
                                            .setHome(home)
                                            .setType(NotificationHologram.TYPE.HOME)
                                            .build();
                                    p.sendMessage(ChatColor.GREEN + "Successfully set home: " + args[1]);
                                    p.sendMessage(ChatColor.DARK_GREEN + "Use /home to access your homes");
                                } else {
                                    p.sendMessage(ChatColor.RED + "A home with that name already exists.");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "You don't have permission to set any more homes.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission to set any homes.");
                        }
                    } else {
                        sendHelpMessage(p);
                    }
                    break;
                }
                default: {
                    sendHelpMessage(p);
                    break;
                }
            }

        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
        }
        return true;
    }

    private void sendHelpMessage(Player p) {
        p.sendMessage(ChatColor.RED + "Usage:");
        p.sendMessage(ChatColor.RED + "/home - opens the homes interface");
        p.sendMessage(ChatColor.RED + "/home set <name> - add a new home");
        p.sendMessage(ChatColor.RED + "/home remove <name> - remove a home");
    }

}
