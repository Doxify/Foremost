package com.andreigeorgescu.foremost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.saphron.nsa.user.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

public class MessageManager {
	
	Foremost plugin;
	
	public MessageManager(Foremost p) {
		plugin = p;
	}
	
	
	public boolean sendMessage(Player sender, Player target, String message) {
		if(sender instanceof Player && target instanceof Player) {
			String senderPrefix = ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix((Player) sender));
			String targetPrefix = ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix((Player) target));
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "(To " + targetPrefix + target.getName() + ChatColor.LIGHT_PURPLE + ") " + message);
    		target.sendMessage(ChatColor.LIGHT_PURPLE + "(From " + senderPrefix + sender.getName() + ChatColor.LIGHT_PURPLE + ") " + message);
			User user = plugin.nsaPlugin.userManager.getUser(sender.getUniqueId().toString());
			User targetUser = plugin.nsaPlugin.userManager.getUser(target.getUniqueId().toString());

			// Sounds
    		if(user.getToggleSounds()) {
    			playMessageSound(sender);
    		}

    		if(targetUser.getToggleSounds()) {
    			playMessageSound(target);
    		}

    		// Backend
    		updateLastReply(sender, target);
//    		logMessage(sender, target, message);
			notifySocialSpys(sender, target, message);
    		return true;
		}
		return false;
	}
	
	public void updateLastReply(Player sender, Player target) {
		plugin.profileManager.getProfile(sender.getUniqueId().toString()).setLastReply(target.getName());
		plugin.profileManager.getProfile(target.getUniqueId().toString()).setLastReply(sender.getName());
	}
	
	public static void playMessageSound(Player target) {
		target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1, 1);
	}

	public void notifySocialSpys(Player sender, Player target, String message) {
		for(UUID uuid : plugin.nsaPlugin.userManager.getSocialSpys()) {
			Player socialSpy = Bukkit.getPlayer(uuid);

			if(socialSpy == null) {
				continue;
			}

			String senderPrefix = ChatColor.translateAlternateColorCodes('&', plugin.nsaPlugin.getChat().getPlayerPrefix((Player) sender));
			String targetPrefix = ChatColor.translateAlternateColorCodes('&', plugin.nsaPlugin.getChat().getPlayerPrefix((Player) target));
			socialSpy.sendMessage(ChatColor.BLUE + "[SS] " + senderPrefix + sender.getName() + ChatColor.LIGHT_PURPLE + " > " + targetPrefix + target.getName() + ChatColor.LIGHT_PURPLE + ": " + message);

		}
	}

	// TODO: Log messages
//	@SuppressWarnings("unchecked")
//	public void logMessage(Player sender, Player target, String message) {
//		JSONObject messageLog = new JSONObject();
//		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
//
//		messageLog.put("sender", sender.getUniqueId().toString());
//		messageLog.put("senderName", sender.getName().toLowerCase());
//		messageLog.put("target", target.getUniqueId().toString());
//		messageLog.put("targetName", target.getName().toLowerCase());
//		messageLog.put("message", message);
//		messageLog.put("timestamp", timestamp);
//		messageLog.put("server", plugin.nsaPlugin.config.getServerName());
//
//		new Document();
//		new BukkitRunnable() {
//    	    public void run() {
//    	    	plugin.nsaPlugin.postMessageLogToDatabase(Document.parse(messageLog.toString()));
//    	    }
//		}.runTaskAsynchronously(plugin);
//	}

}
