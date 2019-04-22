package com.andreigeorgescu.foremost.command;

import com.saphron.nsa.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CondenseCommand implements CommandExecutor {

    private static final List<Material> TYPES = new ArrayList<>(
            Arrays.asList(
                    Material.EMERALD_ORE,
                    Material.DIAMOND_ORE,
                    Material.IRON_ORE,
                    Material.IRON_INGOT,
                    Material.GOLD_ORE,
                    Material.GOLD_INGOT,
                    Material.REDSTONE_ORE,
                    Material.LAPIS_ORE,
                    Material.COAL_ORE,
                    Material.COAL
            ));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("foremost.condense")) {
                Player p = (Player) sender;

                if(p.getItemInHand() != null) {
                    if(TYPES.contains(p.getItemInHand().getType())) {
                        int amount = p.getItemInHand().getAmount();
                        int size = (int) Math.ceil(amount / 9.0);




                    } else {
                        p.sendMessage(ChatColor.RED + "Sorry, this can't be condensed.");
                        return true;
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You must be holding the items you want to condense.");
                    return true;
                }

                return true;
            } else {
                sender.sendMessage(Utilities.NO_PERMISSION);
                return true;
            }
        } else {
            sender.sendMessage(Utilities.NON_PLAYER_SENDER);
            return true;
        }
    }

    // Returns whether or not it condensed any items in the player's
    // inventory.
    private boolean condensePlayerInventory(Player p) {
        boolean condensed = false;

        for(ItemStack item : p.getInventory().getContents()) {
            if(item != null) {
                if(TYPES.contains(item.getType())) {
                    if(item.getAmount() >= 9) {
//                        int
                    }
                }
            }
        }

        return condensed;
    }

//    private boolean
}
