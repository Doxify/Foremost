package com.andreigeorgescu.foremost;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatManager {
	
	boolean chatIsMuted;
	
	public ChatManager() {
		chatIsMuted = false;
	}

    public boolean getChatMuteSetting() {
        return chatIsMuted;
    }

    public boolean muteChat() {
	    if(!chatIsMuted) {
            chatIsMuted = true;
            return true;
        } else {
            return false;
        }
	}
	
	public boolean unmuteChat() {
        if(chatIsMuted) {
            chatIsMuted = false;
            return true;
        } else {
            return false;
        }
	}
	
	public void clearChat(String clearedBy) {
		for (int x = 0; x < 150; x++){
		    Bukkit.broadcastMessage("");
		}
        Bukkit.broadcastMessage(ChatColor.RED + "Chat has been cleared by " + clearedBy + ".");
	}

}
