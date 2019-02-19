package com.andreigeorgescu.foremost.utils;

import com.andreigeorgescu.foremost.kits.Kit;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class KitSerializer {

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);

    public JSONArray serializeKits(List<Kit> kits) {
        JSONArray serializedKits = new JSONArray();
        for(Kit kit : kits) {
            JSONObject jsonKitObject = new JSONObject();
            jsonKitObject.put("name", kit.getName());
            jsonKitObject.put("cooldown", Integer.toString(kit.getCooldown()));
            jsonKitObject.put("kitItems", ItemSerializer.itemStackArrayToBase64(kit.getKit()));
            serializedKits.add(jsonKitObject);
        }
        return serializedKits;
    }

    public List<Kit> deserializeKits(JSONArray jsonKits) throws IOException {
        List<Kit> kits = new ArrayList<>();
        for(int i = 0; i < jsonKits.size(); i++) {
            JSONObject kitObject = (JSONObject) jsonKits.get(i);
            String name = (String) kitObject.get("name");
            int cooldown = Integer.parseInt((String) kitObject.get("cooldown"));
            ItemStack[] kitItems = ItemSerializer.itemStackArrayFromBase64((String) kitObject.get("kitItems"));

            kits.add(new Kit(name, cooldown, kitItems));
        }
        return kits;
    }

    public String dateTimeToString(LocalDateTime date) {
        String dateTimeString = dtf.format(date);
        return dateTimeString;
    }

    public LocalDateTime stringToDateTime(String dts) {
        LocalDateTime dateTime = LocalDateTime.parse(dts, dtf);
        return dateTime;
    }

    public JSONArray serializeCooldowns(HashMap<UUID, HashMap<String, String>> cooldowns) {
        JSONArray serlializedCooldowns = new JSONArray();

        for(UUID uuid : cooldowns.keySet()) {
            JSONObject playerCooldownObject = new JSONObject();
            JSONArray playerCooldownsArray = new JSONArray();

            playerCooldownObject.put("uuid", uuid.toString());

            for(String kitName : cooldowns.get(uuid).keySet()) {
                JSONObject cooldownObject = new JSONObject();
                cooldownObject.put("kit", kitName);
                cooldownObject.put("cooldown", cooldowns.get(uuid).get(kitName));
                playerCooldownsArray.add(cooldownObject);
            }

            playerCooldownObject.put("cooldowns", playerCooldownsArray);
            serlializedCooldowns.add(playerCooldownObject);
        }

        return serlializedCooldowns;

    }

    public HashMap<UUID, HashMap<String, String>> deserializeCooldowns(JSONArray jsonCooldowns) {
        HashMap<UUID, HashMap<String, String>> cooldowns = new HashMap<>();

        for(int i = 0; i < jsonCooldowns.size(); i++) {
            boolean activeCooldowns = false;
            JSONObject cooldownObject = (JSONObject) jsonCooldowns.get(i);
            UUID playerUUID = UUID.fromString((String) cooldownObject.get("uuid"));
            JSONArray cooldownArray = (JSONArray) cooldownObject.get("cooldowns");

            cooldowns.put(playerUUID, new HashMap<>());

            for(int j = 0; j < cooldownArray.size(); j++) {
                JSONObject cooldown = (JSONObject) cooldownArray.get(i);
                String kitName = (String) cooldown.get("kit");
                LocalDateTime cooldownTime = stringToDateTime((String) cooldown.get("cooldown"));
                cooldowns.get(playerUUID).put(kitName, dtf.format(cooldownTime));
            }
        }

        return cooldowns;
    }
}
