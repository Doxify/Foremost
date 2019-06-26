package com.andreigeorgescu.foremost.cooldowns;

import com.andreigeorgescu.foremost.Foremost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    public enum COOLDOWN {
        REPAIR, REPAIRALL, HEAL
    }

    private HashMap<COOLDOWN, HashMap<UUID, Cooldown>> cooldowns;


    public CooldownManager() {
        this.cooldowns = new HashMap<COOLDOWN, HashMap<UUID, Cooldown>>();
        cooldowns.put(COOLDOWN.REPAIR, new HashMap<UUID, Cooldown>());
        cooldowns.put(COOLDOWN.REPAIRALL, new HashMap<UUID, Cooldown>());
        cooldowns.put(COOLDOWN.HEAL, new HashMap<UUID, Cooldown>());
        loadCooldowns();
    }

    public void handleServerClose() {
        saveCooldowns();
        cooldowns.clear();
    }

    private void loadCooldowns() {
        try {
            Object cooldownsFileParsed = new JSONParser().parse(new FileReader("./plugins/Foremost/cooldowns.json"));
            JSONObject cooldownsJson = (JSONObject) cooldownsFileParsed;

            for(COOLDOWN type : cooldowns.keySet()) {
                JSONArray cooldownsArray = (JSONArray) cooldownsJson.get(type.toString());
                for(Object obj : cooldownsArray) {
                    String data = (String) obj;
                    String[] dataArr = data.split(";");
                    UUID uuid = UUID.fromString(dataArr[0]);
                    int remaining = Integer.parseInt(dataArr[1]);

                    cooldowns.get(type).put(uuid, new Cooldown((System.currentTimeMillis() / 1000), remaining));
                }
            }

            Foremost.getPlugin().getLogger().info("Successfully loaded cooldowns!");
            Foremost.getPlugin().getLogger().info("Loaded repair cooldowns: " + cooldowns.get(COOLDOWN.REPAIR).size());
            Foremost.getPlugin().getLogger().info("Loaded repairAll cooldowns: " + cooldowns.get(COOLDOWN.REPAIRALL).size());
            Foremost.getPlugin().getLogger().info("Loaded heal cooldowns: " + cooldowns.get(COOLDOWN.HEAL).size());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            Foremost.getPlugin().getLogger().severe("Failed to load cooldowns.json.");
        }
    }


    private void saveCooldowns() {
        JSONObject cooldownJson = new JSONObject();

        for(COOLDOWN type : cooldowns.keySet()) {
            JSONArray cooldownArray = new JSONArray();
            for(UUID uuid : cooldowns.get(type).keySet()) {
                Cooldown cooldown = cooldowns.get(type).get(uuid);
                if(!cooldown.cooldownExpired()) {
                    String data = uuid.toString() + ";" + cooldown.getRemainingCooldown();
                    cooldownArray.add(data);
                }
            }
            cooldownJson.put(type.toString(), cooldownArray);
        }


        try {
            FileWriter writer = new FileWriter("./plugins/Foremost/cooldowns.json");
            writer.write(cooldownJson.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Foremost.getPlugin().getLogger().severe("There was an error saving to cooldowns.json");
        }


    }


    public boolean addCooldown(COOLDOWN type, UUID uuid, int duration) {
        if(cooldowns.get(type).containsKey(uuid)) {
            Cooldown currentCooldown = cooldowns.get(type).get(uuid);
            if(currentCooldown.cooldownExpired()) {
                Cooldown newCooldown = new Cooldown((System.currentTimeMillis() / 1000), duration);
                cooldowns.get(type).put(uuid, newCooldown);
                return true;
            } else {
                return false;
            }
        } else {
            Cooldown newCooldown = new Cooldown((System.currentTimeMillis() / 1000), duration);
            cooldowns.get(type).put(uuid, newCooldown);
            return true;
        }
    }

    public void removeCooldown(COOLDOWN type, UUID uuid) {
        cooldowns.get(type).remove(uuid);
    }

    public boolean hasCooldown(COOLDOWN type, UUID uuid) {
        if(!cooldowns.get(type).containsKey(uuid)) {
            return false;
        }

        Cooldown cooldown = cooldowns.get(type).get(uuid);
        if (cooldown.cooldownExpired()) {
            removeCooldown(type, uuid);
            return false;
        } else {
            return true;
        }
    }

    public Cooldown getCooldown(COOLDOWN type, UUID uuid) {
        return cooldowns.get(type).get(uuid);
    }



}