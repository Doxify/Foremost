package com.andreigeorgescu.foremost.kits;

import com.andreigeorgescu.foremost.Foremost;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class KitsManager {

    Foremost plugin;

    private boolean kitsEnabled;

    public KitsManager(Foremost p) {
        plugin = p;
        if(plugin.config.getKits().size() > 0) {
            kitsEnabled = true;
        }
    }

    public boolean deleteKit(String kitName) {
        for(Kit kit : plugin.config.getKits()) {
            if(kit.getName().equalsIgnoreCase(kitName)) {
                plugin.config.getKits().remove(kit);

                if(plugin.config.getKits().size() == 0) {
                    kitsEnabled = false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean addKit(String kitName, ItemStack[] kitItems, int cooldown) {
        boolean sameKitDetected = false;
        for(Kit kit : plugin.config.getKits()) {
            if(kit.getName().equalsIgnoreCase(kitName)) {
                sameKitDetected = true;
                break;
            }
        }

        if(!sameKitDetected) {
            Kit newKit = new Kit(kitName, cooldown, kitItems);
            plugin.config.getKits().add(newKit);
            if(!kitsEnabled) {
                kitsEnabled = true;
            }
            return true;
        }

        return false;
    }


    public String getKitList(Player p) {
        boolean firstKit = true;
        String kitList = "";

        if(!kitsEnabled) {
            return ChatColor.RED + "There are currently no kits available.";
        }

        for(Kit kit : plugin.config.getKits()) {
            // Kit permission format: foremost.kit.kitName
            if(p.hasPermission("foremost.kit." + kit.getName())) {
                if(firstKit) {
                    kitList += ChatColor.GREEN + kit.getName();
                    firstKit = false;
                } else {
                    kitList += ChatColor.GRAY + ", " + ChatColor.GREEN + kit.getName();
                }
            } else {
                if(firstKit) {
                    kitList += ChatColor.RED + kit.getName();
                    firstKit = false;
                } else {
                    kitList += ChatColor.GRAY + ", " + ChatColor.RED + kit.getName();
                }
            }
        }
        return kitList;
    }

    public boolean kitExists(String kitName) {
        for(Kit kit : plugin.config.getKits()) {
            if(kit.getName().equalsIgnoreCase(kitName)) {
                return true;
            }
        }
        return false;
    }

    public Kit getKitObject(String kitName) {
        if(kitExists(kitName)) {
            for(Kit kit : plugin.config.getKits()) {
                if(kit.getName().equalsIgnoreCase(kitName)) {
                    return kit;
                }
            }
        }
        return null;
    }

    public boolean givePlayerKit(Player p, Kit kit) {
        if(p.hasPermission("foremost.kit." + kit.getName())) {

            p.updateInventory();
            return true;
        }
        return false;
    }
}
