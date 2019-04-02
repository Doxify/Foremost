package com.andreigeorgescu.foremost.waraps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class WarpManager {

    private static Map<String, Warp> warps;
    private static Inventory warpMenu = null;

    // Default constructor
    public WarpManager() {
        warps = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        if(loadWarpsFromFile()) {
            warpMenu = loadWarpMenu();
        }

    }

    // Tasks to run on server close
    public void handleServerClose() {
        saveWarpsToFile();
        warps.clear();
    }

    // Loads warps from warps.json and adds them to the map
    private boolean loadWarpsFromFile() {
        try {
            Object warpsRaw = new JSONParser().parse(new FileReader("./plugins/Foremost/warps.json"));
            JSONObject warpsRawJson = (JSONObject) warpsRaw;
            JSONArray warpsJsonArray = (JSONArray) warpsRawJson.get("warps");

            for(Object warpObject : warpsJsonArray) {
                JSONObject warpJson = (JSONObject) warpObject;
                Warp warp = new Warp(warpJson);
                warps.put(warp.getName(), warp);
                System.out.println("[Foremost] Loaded warp: " + warp.getName());
            }

            if(warps.size() > 0) {
                return true;
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("[Foremost] Failed to load warps.json");
            return false;
        }

        return false;
    }

    // Saves currently laoded warps to file
    private void saveWarpsToFile() {
        try {
            JSONObject warpFile = new JSONObject();
            JSONArray warpsArray = new JSONArray();
            for(Warp warp : warps.values()) {
                warpsArray.add(warp.serialize());
            }

            warpFile.put("warps", warpsArray);

            FileWriter writer = new FileWriter("./plugins/Foremost/warps.json");
            writer.write(warpFile.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[foremost] Failed to save warps.json");
        }
    }

    // Loads a warp gui based on the current waps loaded into the MAP
    private Inventory loadWarpMenu() {
        if(warps.size() > 0) {
            int inventorySize = 9 * (int) Math.ceil(warps.size() / 9.0);
            Inventory warpInventory = Bukkit.createInventory(null, inventorySize, "Warp Menu");
            int inventoryCounter = 0;

            for(String warpName : warps.keySet()) {
                Warp warp = warps.get(warpName);
                Location warpLocation = warp.getLocation();

                ItemStack warpItemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta warpItemStackMeta = warpItemStack.getItemMeta();

                warpItemStackMeta.setDisplayName(ChatColor.YELLOW + warp.getName());
                warpItemStackMeta.setLore(Arrays.asList(
                        ChatColor.GRAY + "World: " + ChatColor.YELLOW + warpLocation.getWorld().getName(),
                        ChatColor.GRAY + "Coordinates: " + ChatColor.YELLOW + warpLocation.getBlockX() + ", " + warpLocation.getBlockY() + ", " + warpLocation.getBlockZ()
                ));
                warpItemStack.setItemMeta(warpItemStackMeta);

                warpInventory.setItem(inventoryCounter, warpItemStack);
                inventoryCounter++;
            }

            return warpInventory;
        } else {
            return null;
        }
    }

    // Returns the warps gui if it exists, null if it doesn't
    public Inventory getWarpMenu() {
        return warpMenu;
    }

    // Returns true if successful, false if a warp with that name already exists
    public boolean addWarp(String name, Location location) {
        if(!warps.containsKey(name)) {
            Warp warp = new Warp(name, location);
            warps.put(name, warp);
            warpMenu = loadWarpMenu();
            return true;
        }
        return false;
    }

    // Returns true if successful, false if a warp with that name doesn't exist
    public boolean removeWarp(String name) {
        if(warps.containsKey(name)) {
            warps.remove(name);
            warpMenu = loadWarpMenu();
            return true;
        }
        return false;
    }

    // Returns a a warp object if it exists, otherwise it returns null
    public Warp getWarp(String warpName) {
        if(warps.containsKey(warpName)) {
            Warp warp = warps.get(warpName);
            return warp;
        }
        return null;
    }



}
