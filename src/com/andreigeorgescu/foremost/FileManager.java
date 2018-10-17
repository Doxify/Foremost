package com.andreigeorgescu.foremost;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
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
			
			JSONObject spawnJson = (JSONObject) configJson.get("spawn");
			Location spawnLocation = null;
			if(spawnJson != null) {
				spawnLocation = deserializeLocation((JSONObject) configJson.get("spawn"));
			}
			
			JSONObject warpsJson = (JSONObject) configJson.get("warps");
			HashMap<String, Location> warps = new HashMap<String, Location>();
			if(warpsJson != null) {
				warps = deserializeWarps((JSONObject) configJson.get("warps"));
			}
			
			Config configClass = new Config(spawnLocation, warps);
			return configClass;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			System.out.println("[Foremost] Failed to load config.json. Returned a null Config.");
			return new Config(null, null);
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
		serializedObj.put("x" , locToSerialize.getX());
		serializedObj.put("y" , locToSerialize.getY());
		serializedObj.put("z" , locToSerialize.getZ());
		serializedObj.put("yaw", locToSerialize.getYaw());
		serializedObj.put("pitch", locToSerialize.getPitch());

		
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
		Location locToDeserialize = new Location(plugin.getServer().getWorld((String) jsonLocation.get("world")), (double) jsonLocation.get("x"), (double) jsonLocation.get("y"), (double) jsonLocation.get("z"), ((Double) jsonLocation.get("yaw")).floatValue(), ((Double) jsonLocation.get("pitch")).floatValue());
		return locToDeserialize;
	}
	
			
}
