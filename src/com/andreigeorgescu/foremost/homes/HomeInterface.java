package com.andreigeorgescu.foremost.homes;

import com.saphron.nsa.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeInterface {

    public static Inventory getHomeInventory(List<Home> homes, int maxHomes) {
        int size = 9 * (int) Math.ceil(maxHomes / 9.0);
        Inventory homeInventory = Bukkit.createInventory(null, size, "Homes " + "(" + homes.size() + "/" + maxHomes + ")");

        for(Home home : homes) {
            Location homeLocation = home.getLocation();

            ItemStack homeItem = Utilities.createGuiItem(
                    ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + home.getName(),
                    new ArrayList<>(Arrays.asList(
//                            ChatColor.GRAY + "World: " + ChatColor.YELLOW + homeLocation.getWorld().getName(),
                            ChatColor.GRAY + "Coordinates: " + ChatColor.YELLOW + homeLocation.getBlockX() + ", " + homeLocation.getBlockY() + ", " + homeLocation.getBlockZ(),
                            ChatColor.GRAY + "Cooldown: " + (home.isOnCooldown() ? ChatColor.RED + "Yes" : ChatColor.GREEN + "No"),
                            "",
                            ChatColor.LIGHT_PURPLE + "Click to teleport to " + home.getName()
                    )),
                    Material.NAME_TAG,
                    0
            );

            if(home.isOnCooldown()) {
                homeItem.setType(Material.STAINED_GLASS_PANE);
                homeItem.setDurability((short) 14);
            }

            homeInventory.addItem(homeItem);
        }

        if(homes.size() < size) {
            for(int i = homes.size(); i < size; i++) {
                if(i >= maxHomes) {
                    homeInventory.setItem(i, Utilities.createGuiItem(
                            ChatColor.WHITE + " ",
                            null,
                            Material.STAINED_GLASS_PANE,
                            7
                    ));
                } else {
                    homeInventory.setItem(i, Utilities.createGuiItem(
                            ChatColor.WHITE + " ",
                            null,
                            Material.STAINED_GLASS_PANE,
                            5
                    ));
                }
            }
        }

        return homeInventory;
    }
}
