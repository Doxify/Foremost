package com.andreigeorgescu.foremost.homes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Home {

    private String name;
    private Location location;
    private UUID owner;
    private List<UUID> members;
    private boolean cooldown;

    // Main Constructor
    public Home(String name, Location location, UUID owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.members = new ArrayList<>();
    }

    // Used for loading homes from file
    public Home(JSONObject object) {
        deserialize(object);
    }


    // Getters / Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        OfflinePlayer p = Bukkit.getOfflinePlayer(owner);
        return p.getName();
    }

    public void setCooldown(boolean cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isOnCooldown() {
        return cooldown;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // serialization and deserialization
    public JSONObject serialize() {
        JSONObject homeJson = new JSONObject();
        StringBuilder locationString = new StringBuilder();
        locationString.append(location.getWorld().getName());
        locationString.append(";" + location.getX());
        locationString.append(";" + location.getY());
        locationString.append(";" + location.getZ());
        locationString.append(";" + location.getYaw());
        locationString.append(";" + location.getPitch());

        homeJson.put("name", name);
        homeJson.put("owner", owner.toString());
        homeJson.put("location", locationString.toString());

        return homeJson;
    }

    // Deserializes a JSONObject and initializes the values
    public void deserialize(JSONObject homeObject) {
        String name = (String) homeObject.get("name");
        UUID owner = UUID.fromString((String) homeObject.get("owner"));
        String[] locationRaw = ((String) homeObject.get("location")).split(";");

        this.name = name;
        this.owner = owner;
        this.location = new Location(Bukkit.getWorld(locationRaw[0]), Double.parseDouble(locationRaw[1]), Double.parseDouble(locationRaw[2]), Double.parseDouble(locationRaw[3]), Float.parseFloat(locationRaw[4]), Float.parseFloat(locationRaw[5]));
    }

}
