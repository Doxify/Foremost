package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.give")) {
                if(label.equalsIgnoreCase("give")) {
                    switch(args.length) {
                        case 3:
                            Player target = Bukkit.getServer().getPlayer(args[0]);
                            if(target != null) {
                                final String itemname = args[1].toUpperCase().replace("_", "");
                                try {
                                    ItemStack targetItem = new ItemStack(Material.valueOf(itemname), Integer.parseInt(args[2]));
                                    target.getInventory().addItem(targetItem);
                                    target.updateInventory();
                                    sender.sendMessage(ChatColor.GREEN + "Gave " + target.getName() + " " + args[2] + " " + itemname + ".");
                                    return true;
                                } catch (NullArgumentException err) {
                                    err.printStackTrace();
                                    return true;
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "Usage: /give <playername> <item> <amount>");
                            break;
                    }
                    return true;
                } else if(label.equalsIgnoreCase("i")) {
                    switch(args.length) {
                        case 2:
                            final String itemname = args[1].toUpperCase().replace("_", "");
                            try {
                                ItemStack targetItem = new ItemStack(Material.valueOf(itemname), Integer.parseInt(args[2]));
                                ((Player) sender).getInventory().addItem(targetItem);
                                ((Player) sender).updateInventory();
                                sender.sendMessage(ChatColor.GREEN + "You received " + args[2] + " " + itemname + ".");
                            } catch (NullArgumentException err) {
                                err.printStackTrace();
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "Usage: /i <item> <amount>");
                            break;
                    }

                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /give <playername> <item> <amount>"+ ChatColor.GRAY + " Gives items to other players" + ChatColor.RED +"\nUsage: /i <item> <amount>" + ChatColor.GRAY + " Gives you items.");
                    return true;
                }


            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
                return true;
            }
        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
            return true;
        }
    }

}
