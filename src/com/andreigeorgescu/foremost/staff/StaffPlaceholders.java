package com.andreigeorgescu.foremost.staff;

import com.andreigeorgescu.foremost.Foremost;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StaffPlaceholders extends PlaceholderExpansion {

    private static int queuedCount = 0;

    @Override
    public boolean register(){
        return super.register();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "Andrei Georgescu";
    }

    @Override
    public String getIdentifier() {
        return "staff";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if(p == null) {
            return "";
        }

        if(identifier.equalsIgnoreCase("online")) {
            return String.valueOf(Foremost.getPlugin().staffManager.getOnlineStaff());
        }

        if(identifier.equalsIgnoreCase("vanished")) {
            if(Foremost.getPlugin().staffManager.isStaffMode(p)) {
                StaffModePlayer staffModePlayer = Foremost.getPlugin().staffManager.getStaffModePlayer(p);
                if(staffModePlayer.isVanished()) {
                    return ChatColor.GREEN + "True";
                } else {
                    return ChatColor.RED + "False";
                }
            }
        }

        if(identifier.equalsIgnoreCase("queued")) {
            return String.valueOf(queuedCount);
        }

        return null;
    }

    public static void setQueuedCount(int count) { queuedCount = count; }
    public static int getQueuedCount() { return queuedCount; }

}
