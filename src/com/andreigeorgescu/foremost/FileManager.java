package com.andreigeorgescu.foremost;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import com.andreigeorgescu.foremost.kits.Kit;
import com.andreigeorgescu.foremost.kits.KitsManager;
import com.andreigeorgescu.foremost.utils.KitSerializer;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class FileManager {
	
	Foremost plugin;
	KitSerializer kitSerializer = new KitSerializer();
	
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
			
			Config configClass = new Config(spawnLocation, warps);
			return configClass;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("[Foremost] Failed to load config.json. Returned a null Config.");
			return new Config(null, null);
		}
	}

	public KitsManager loadKitsManager() {
		List<Kit> kits = new ArrayList<>();
		HashMap<UUID, HashMap<String, String>> cooldowns = new HashMap<>();

		try {
			Object kitFileParsed = new JSONParser().parse(new FileReader("./plugins/Foremost/kits.json"));
			JSONArray kitJson = (JSONArray) kitFileParsed;
			kits = kitSerializer.deserializeKits(kitJson);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("[Foremost] Failed to load kits.json. Returned an empty List.");
		}

		try {
			Object cooldownFileParsed = new JSONParser().parse(new FileReader("./plugins/Foremost/cooldowns.json"));
			JSONArray cooldownJson = (JSONArray) cooldownFileParsed;
			cooldowns = kitSerializer.deserializeCooldowns(cooldownJson);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("[Foremost] Failed to load cooldowns.json. Returned an empty HashMap.");
		}

		return new KitsManager(kits, cooldowns);

	}

	public void saveKitsToFile() {
		JSONArray kitsJSON = kitSerializer.serializeKits(plugin.getKitsManager().getKits());
		JSONArray cooldownJSON = kitSerializer.serializeCooldowns(plugin.getKitsManager().getCooldowns());

		try {
			FileWriter writer = new FileWriter("./plugins/Foremost/kits.json");
			writer.write(kitsJSON.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Foremost] There was an error saving to kits.json");
		}

		try {
			FileWriter writer = new FileWriter("./plugins/Foremost/cooldowns.json");
			writer.write(cooldownJSON.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Foremost] There was an error saving to cooldowns.json");
		}

	}
	
	// =============================================
    // Saving a config file
    // =============================================
	public void saveConfigFile(String filePath, String fileName, Config config) {
		JSONObject configJSON = new JSONObject();
		JSONObject spawnJSON;
		JSONObject warpsJSON;

		if(config.getSpawn() instanceof Location) {
			spawnJSON = serializeLocation(config.getSpawn());
			configJSON.put("spawn", spawnJSON);
		}
		
		if(config.getWarps() instanceof HashMap) {
			warpsJSON = serializeWarps(config.getWarps());
			configJSON.put("warps", warpsJSON);
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
