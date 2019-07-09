package com.andreigeorgescu.foremost.staff.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.staff.StaffPlaceholders;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ForemostQueueListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("ForemostQueue")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if(subChannel.equals("Queued")) {
            String targetServer = in.readUTF();
            if(targetServer.equalsIgnoreCase(Foremost.getPlugin().nsaPlugin.config.getServerName())) {
                try {
                    int num = Integer.parseInt(in.readUTF());
                    StaffPlaceholders.setQueuedCount(num);
                } catch (NumberFormatException e) {
                    StaffPlaceholders.setQueuedCount(0);
                }
            }
        }
    }
}
