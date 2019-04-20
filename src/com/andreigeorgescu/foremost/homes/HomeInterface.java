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
                            ChatColor.GRAY + "World: " + ChatColor.YELLOW + homeLocation.getWorld().getName(),
                            ChatColor.GRAY + "Coordinates: " + ChatColor.YELLOW + homeLocation.getBlockX() + ", " + homeLocation.getBlockY() + ", " + homeLocation.getBlockZ(),
                            "",
                            ChatColor.YELLOW + "Click to teleport to" + home.getName()
                    )),
                    Material.NAME_TAG
            );

            homeInventory.addItem(homeItem);
        }

        return homeInventory;
    }
}
