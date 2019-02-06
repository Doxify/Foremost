package com.andreigeorgescu.foremost;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.andreigeorgescu.foremost.kits.ItemSerializer;
import com.andreigeorgescu.foremost.kits.Kit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class FileManager {
	
	Foremost plugin;
	
	public FileManager(Foremost p) {
		plugin = p;
	}
	
	// =============================================
    // Loading a config file
    // =============================================
	public Config loadConfigFile(String filePath, String fileName) {
		try {
			Object configFileParsed = new JSONParser().parse(new FileReader(filePath + "/" + fileName));
			JSONObject configJson = (JSONObject) configFileParsed;
			HashMap<String, Location> warps = new HashMap<>();
			Location spawnLocation = null;
			List<Kit> kits = new ArrayList<>();

			try {
				spawnLocation = deserializeLocation((JSONObject) configJson.get("spawn"));
			} catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
				e.printStackTrace();
				System.out.println("[Foremost] Could not load spawn json, setting it to null.");
			}

			try {
				warps = deserializeWarps((JSONObject) configJson.get("warps"));
			} catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
				e.printStackTrace();
				System.out.println("[Foremost] Could not load warps json, setting them to null.");
			}

			try {
				kits = deserializeKits((JSONArray) configJson.get("kits"));
			} catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
				e.printStackTrace();
				System.out.print("[Foremost] Could not load kits json, setting them to null.");
			}
			
			Config configClass = new Config(spawnLocation, warps, kits);
			return configClass;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("[Foremost] Failed to load config.json. Returned a null Config.");
			return new Config(null, null, null);
		}
	}
	
	// =============================================
    // Saving a config file
    // =============================================
	public void saveConfigFile(String filePath, String fileName, Config config) {
		JSONObject configJSON = new JSONObject();
		JSONObject spawnJSON;
		JSONObject warpsJSON;
		JSONArray kitsJSON;

		if(config.getSpawn() instanceof Location) {
			spawnJSON = serializeLocation(config.getSpawn());
			configJSON.put("spawn", spawnJSON);
		}
		
		if(config.getWarps() instanceof HashMap) {
			warpsJSON = serializeWarps(config.getWarps());
			configJSON.put("warps", warpsJSON);
		}

		if(config.getKits() instanceof List) {
			kitsJSON = serializeKits(config.getKits());
			configJSON.put("kits", kitsJSON);
		}

		try {
			//write converted json data to a file named "CountryGSON.json"
			FileWriter writer = new FileWriter(filePath + "/" + fileName);
		   	writer.write(configJSON.toString());
		   	writer.close();
			System.out.print("Changes saved!");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Foremost] Failed to save Config class to config.json.");
		}
			  
	}
	
	// =============================================
    // Serializing
    // =============================================
	@SuppressWarnings("unchecked")
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


	@SuppressWarnings("unchecked")
	public JSONObject serializeWarps(HashMap<String, Location> warps) {
		JSONObject serializedWarps = new JSONObject();
		
		for (Map.Entry<String, Location> warp : warps.entrySet()) {
		    String name = warp.getKey();
		    Location location = warp.getValue();
		    serializedWarps.put(name, serializeLocation(location));
		}
		
		return serializedWarps;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject serializeLocation(Location locToSerialize) {
		JSONObject serializedObj = new JSONObject();
		
		serializedObj.put("world" , locToSerialize.getWorld().getName());
		serializedObj.put("x" , String.valueOf(locToSerialize.getX()));
		serializedObj.put("y" , String.valueOf(locToSerialize.getY()));
		serializedObj.put("z" , String.valueOf(locToSerialize.getZ()));
		serializedObj.put("yaw", String.valueOf(locToSerialize.getYaw()));
		serializedObj.put("pitch", String.valueOf(locToSerialize.getPitch()));

		
		return serializedObj;
	}
	
	// =============================================
    // Deserializing
    // =============================================
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


	public HashMap<String, Location> deserializeWarps(JSONObject jsonWarps) {
		HashMap<String, Location> warps = new HashMap<>();
        @SuppressWarnings("unchecked")
		Set<String> names = jsonWarps.keySet();

		for(String name : names) {
			warps.put(name, deserializeLocation((JSONObject) jsonWarps.get(name)));
		}
		
		return warps;
	}
	
	public Location deserializeLocation(JSONObject jsonLocation) {
		Location locToDeserialize = new Location(plugin.getServer().getWorld(
			(String) jsonLocation.get("world")),
			Double.parseDouble((String) jsonLocation.get("x")),
			Double.parseDouble((String) jsonLocation.get("y")),
			Double.parseDouble((String) jsonLocation.get("z")),
			Float.parseFloat((String) jsonLocation.get("yaw")),
			Float.parseFloat((String) jsonLocation.get("pitch"))
		);
		return locToDeserialize;
	}
	
			
}
