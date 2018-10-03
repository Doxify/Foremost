package com.andreigeorgescu.foremost;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatManager {
	
	boolean chatIsMuted;
	
	public ChatManager() {
		chatIsMuted = false;
	}
	
	public void muteChat(String mutedBy) {
		chatIsMuted = true;
		Bukkit.broadcastMessage(ChatColor.RED + "Chat has been muted by " + mutedBy + ".");

	}
	
	public void unmuteChat(String mutedBy) {
		chatIsMuted = false;
		Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been unmuted by " + mutedBy + ".");

	}
	
	public void clearChat(String clearedBy) {
		for (int x = 0; x < 150; x++){
		    Bukkit.broadcastMessage("");
		}
		Bukkit.broadcastMessage(ChatColor.RED + "Chat has been cleared by " + clearedBy + ".");
	}
	
	public boolean getChatMuteSetting() {
		return chatIsMuted;
	}

}
