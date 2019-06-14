package com.andreigeorgescu.foremost.waraps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.Arrays;


public class Warp {

    private String name;
    private Location location;
    private ItemStack itemStack;

    // Main Constructor
    public Warp(String n, Location l) {
        this.name = n;
        this.location = l;
        createItemStack();
    }

    // Constructor used for loading warps from JSON
    public Warp(JSONObject warpObject) {
        deserialize(warpObject);
        createItemStack();
    }

    // Get a warp's name
    public String getName() {
        return name;
    }

    // Get a warp's location
    public Location getLocation() {
        return location.clone();
    }

    // Returns true if a player has permission to warp, false if not
    public boolean hasPermission(Player p) {
        if(p.hasPermission("foremost.warp." + name)) {
            return true;
        }
        return false;
    }

    private void createItemStack() {
        ItemStack warpItem = new ItemStack(Material.NAME_TAG);
        ItemMeta warpItemMeta = warpItem.getItemMeta();
        warpItemMeta.setDisplayName(ChatColor.YELLOW + name);
        warpItemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "World: " + ChatColor.YELLOW + location.getWorld().getName(),
                ChatColor.GRAY + "Coordinates: " + ChatColor.YELLOW + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
        ));

        warpItem.setItemMeta(warpItemMeta);
        this.itemStack = warpItem;
    }

    public final ItemStack getItemStack() {
        return this.itemStack.clone();
    }

    // Serializes the warp object into a JSONObject
    public JSONObject serialize() {
        JSONObject warpJson = new JSONObject();
        StringBuilder locationString = new StringBuilder();
        locationString.append(location.getWorld().getName());
        locationString.append(";" + location.getX());
        locationString.append(";" + location.getY());
        locationString.append(";" + location.getZ());
        locationString.append(";" + location.getYaw());
        locationString.append(";" + location.getPitch());

        warpJson.put("name", name);
        warpJson.put("location", locationString.toString());

        return warpJson;
    }

    // Deserializes a JSONObject and initializes the values
    public void deserialize(JSONObject warpObject) {
        String[] locationRaw = ((String) warpObject.get("location")).split(";");
        this.name = (String) warpObject.get("name");
        this.location = new Location(Bukkit.getWorld(locationRaw[0]), Double.parseDouble(locationRaw[1]), Double.parseDouble(locationRaw[2]), Double.parseDouble(locationRaw[3]), Float.parseFloat(locationRaw[4]), Float.parseFloat(locationRaw[5]));
    }

}
