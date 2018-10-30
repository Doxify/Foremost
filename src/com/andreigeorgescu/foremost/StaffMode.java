package com.andreigeorgescu.foremost;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class StaffMode {

    private UUID playerUUID;
    private ItemStack[] previousInventory;
    private ItemStack[] previousArmorInventory;
    private Location previousLocation;


    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public StaffMode(String uuid) {
        this.playerUUID = playerUUID.fromString(uuid);
        storePlayerLocation();
        storePlayerInventory();
        givePlayerStaffModeInventory();
    }

    public void quitStaffMode() {
        Player p = Bukkit.getPlayer(playerUUID);
        if(p != null) {
            p.getInventory().clear();
            p.getInventory().setArmorContents(previousArmorInventory);
            p.getInventory().setContents(previousInventory);
            p.teleport(previousLocation);
        }
    }

    private void storePlayerLocation() {
        Player p = Bukkit.getPlayer(playerUUID);
        if(p != null) {
            previousLocation = p.getLocation();
        }
    }

    private void storePlayerInventory() {
        Player p = Bukkit.getPlayer(playerUUID);
        if(p != null) {
            previousArmorInventory = p.getInventory().getArmorContents();
            previousInventory = p.getInventory().getContents();
        }
    }

    private void givePlayerStaffModeInventory() {
        Player p = Bukkit.getPlayer(playerUUID);
        if(p != null) {
            p.getInventory().clear();
            generateStaffModeInventory(p);
        }
    }

    private void generateStaffModeInventory(Player p) {
        p.getInventory().setItem(0, createGuiItem(ChatColor.RED + "Spawn", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Teleport to spawn")), Material.SIGN, 0));
        p.getInventory().setItem(1, createGuiItem(ChatColor.RED + "Teleporter", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Right Click: " + ChatColor.RED + "Teleport through walls", ChatColor.GRAY + "Left Click: " + ChatColor.RED + "Teleport to a block")), Material.COMPASS, 0));
        p.getInventory().setItem(2, createGuiItem(ChatColor.RED + "Random Teleport", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Teleport to a random player")), Material.NETHER_STAR, 0));
        p.getInventory().setItem(7, createGuiItem(ChatColor.RED + "Server Tools", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Manage the server with various tools")), Material.BOOK, 0));
        p.getInventory().setItem(8, createGuiItem(ChatColor.RED + "Online Staff", new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "View online staff")), Material.PAPER, 0));
    }

    private ItemStack createGuiItem(String name, ArrayList<String> lore, Material mat, int meta) {
        ItemStack item = new ItemStack(mat, 1, (byte) meta);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }


}
