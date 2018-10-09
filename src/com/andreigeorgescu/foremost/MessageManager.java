package com.andreigeorgescu.foremost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

public class MessageManager {
	
	Foremost plugin;
	
	public MessageManager(Foremost p) {
		plugin = p;
	}
	
	
	public boolean sendMessage(Player sender, Player target, String message) {
		if(sender instanceof Player && target instanceof Player) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "(To " + target.getName() + ") " + message);
    		target.sendMessage(ChatColor.LIGHT_PURPLE + "(From " + sender.getName() + ") " + message);
    		
    		if(plugin.nsaPlugin.getProfileManager().getProfile(sender.getUniqueId().toString()).getToggleSound()) {
    			playMessageSound(sender);
    		}
    		if(plugin.nsaPlugin.getProfileManager().getProfile(target.getUniqueId().toString()).getToggleSound()) {
    			playMessageSound(target);
    		}
    		
    		updateLastReply(sender, target);
    		logMessage(sender, target, message);
			notifySocialSpys(sender, target, message);
    		return true;
		} else {
			// TODO: handle errors here
			return false;
		}
	}
	
	public void updateLastReply(Player sender, Player target) {
		plugin.profileManager.getProfile(((Player) sender).getUniqueId().toString()).setLastReply(target.getName());
		plugin.profileManager.getProfile(target.getUniqueId().toString()).setLastReply(sender.getName());
	}
	
	public void playMessageSound(Player target) {
		target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1, 1);
	}
	
	public void notifySocialSpys(Player sender, Player target, String message) {
		for(String uuid : plugin.nsaPlugin.getProfileManager().getSocialSpys()) {
			UUID uuidFromString = UUID.fromString(uuid);
			Player socialSpy = Bukkit.getPlayer(uuidFromString);
			if(socialSpy != null) {
				socialSpy.sendMessage(ChatColor.BLUE + "[SS] " + sender.getName() + " > " + target.getName() + ": " + message);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void logMessage(Player sender, Player target, String message) {
		JSONObject messageLog = new JSONObject();
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
		
		messageLog.put("sender", sender.getUniqueId().toString());
		messageLog.put("senderName", sender.getName().toLowerCase());
		messageLog.put("target", target.getUniqueId().toString());
		messageLog.put("targetName", target.getName().toLowerCase());
		messageLog.put("message", message);
		messageLog.put("timestamp", timestamp);
		messageLog.put("server", plugin.nsaPlugin.getServerName());
		
		new Document();
		new BukkitRunnable() {
    	    public void run() {
    	    	plugin.nsaPlugin.postMessageLogToDatabase(Document.parse(messageLog.toString()));
    	    }
		}.runTaskAsynchronously(plugin);
	}

}