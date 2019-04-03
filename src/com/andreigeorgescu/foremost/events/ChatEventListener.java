package com.andreigeorgescu.foremost.events;

import com.andreigeorgescu.foremost.Foremost;
import com.andreigeorgescu.foremost.utils.ValueFormat;
import com.saphron.nsa.Utilities;
import com.saphron.nsa.user.User;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ChatEventListener implements Listener {

    private final Foremost plugin;
    public ChatEventListener(Foremost p) {
        this.plugin = p;
    }

    @EventHandler ( priority = EventPriority.MONITOR )
    public void onChatMessage(AsyncPlayerChatEvent e) {
        if(!e.isCancelled()) {
            Player p = e.getPlayer();
            if(!plugin.chatManager.getChatMuteSetting() || p.hasPermission("foremost.chat.bypass")) {
                e.setCancelled(true);
                TextComponent message = createChatComponent(e);
                sendMessageToAllPlayers(message);
            } else {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "Chat is currently muted, please try again once chat is unmuted.");
            }
        }
    }

    public TextComponent createChatComponent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        User user = plugin.nsaPlugin.userManager.getUser(p.getUniqueId().toString());
        String formattedChatMessage = handleChatMessageFormatting(p, user, e.getMessage());
        TextComponent formattedTextComponent = handleTextComponentFormatting(p, user, formattedChatMessage);
        ComponentBuilder hoverComponent = getMessageHoverComponent(p, user);
        URL messageURL = getUrlFromMessage(e.getMessage());

        formattedTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent.create()));
        if(messageURL != null) {
            formattedTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, messageURL.toString()));
        }
        return formattedTextComponent;
    }

    private String handleChatMessageFormatting(Player p, User user, String message) {
        String[] unformattedMessageArr = message.split(" ");
        StringBuilder formattedMessage = new StringBuilder();
        boolean canFormatChat = p.hasPermission("foremost.chat.color.message");
        String chatColor = ChatColor.translateAlternateColorCodes('&', user.getChatColor());

        // If the user can format their chat messages then we will format each arg
        // individually keep track of the previous chat color to apply it to the next
        // arg as the user probably won't supply a format for each arg.
        if(canFormatChat) {
            if(message.contains("&")) {
                String lastChatColor = ChatColor.WHITE.toString();
                for(String arg : unformattedMessageArr) {
                    if(arg.contains("&")) {
                        String formatted = ChatColor.translateAlternateColorCodes('&', arg);
                        lastChatColor = ChatColor.getLastColors(formatted);
                        formattedMessage.append(formatted + " ");
                    } else {
                        formattedMessage.append(lastChatColor + arg + " ");
                    }
                }
                return formattedMessage.toString();
            } else {
                for(String arg : unformattedMessageArr) {
                    formattedMessage.append(chatColor + arg + " ");
                }
                return formattedMessage.toString();
            }
        // If the user can't format their own chat messages, then we will just apply the chat color
        // that is stored in the database.
        } else {
            for(String arg : unformattedMessageArr) {
                formattedMessage.append(chatColor + arg + " ");
            }
            return formattedMessage.toString();
        }
    }

    private TextComponent handleTextComponentFormatting(Player p, User user, String message) {
        String playerGroup = plugin.getPerms().getPrimaryGroup(p);
        String groupPrefixRaw = plugin.getChat().getGroupPrefix(p.getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);
        TextComponent textComponent = new TextComponent(groupPrefixFormatted + (user.getNickname() == null ? p.getName() : user.getNickname()) + ChatColor.WHITE + ": " + message);
        return textComponent;
    }


    private ComponentBuilder getMessageHoverComponent(Player p, User user) {
        StringBuilder hoverComponent = new StringBuilder();
        String playerGroup = plugin.getPerms().getPrimaryGroup(p);
        String groupPrefixRaw = plugin.getChat().getGroupPrefix(p.getName(), playerGroup);
        String groupPrefixFormatted = ChatColor.translateAlternateColorCodes('&', groupPrefixRaw);

        hoverComponent.append(groupPrefixFormatted + p.getName() + "\n");
        hoverComponent.append(ChatColor.GRAY + "● " + ChatColor.WHITE + "Join Date: " + ChatColor.GREEN + user.getFirstJoinDate() + "\n");
        hoverComponent.append(ChatColor.GRAY + "● " + ChatColor.WHITE + "Island Level: " + ChatColor.GREEN + getIslandLevelFormatted(p.getUniqueId()) + "\n");
        hoverComponent.append(ChatColor.GRAY + "● " + ChatColor.WHITE + "Money: " + ChatColor.GREEN + Utilities.moneyFormat.format(plugin.getEcon().getBalance(p)));

        return new ComponentBuilder(hoverComponent.toString());
    }

    private URL getUrlFromMessage(String message) {
        String[] chat = message.split(" ");

        for(String msg : chat) {
            try {
                URL url = new URL(msg);
                return url;
            } catch (MalformedURLException ignore) { }
        }

        return null;
    }

    private void sendMessageToAllPlayers(TextComponent message) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(message);
        }
    }

    private String getIslandLevelFormatted(UUID uuid) {
        long islandLevel = ASkyBlockAPI.getInstance().getLongIslandLevel(uuid);
        if(islandLevel >= 1000000000000L) {
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.TRILLIONS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else if(islandLevel >= 1000000000L) {
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.BILLIONS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else if(islandLevel >= 1000000L) {
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.MILLIONS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else if(islandLevel >= 1000){
            int settings = ValueFormat.COMMAS | ValueFormat.PRECISION(2) | ValueFormat.THOUSANDS;
            String formatted = ValueFormat.format(islandLevel, settings);
            return formatted;
        } else {
            return String.valueOf(islandLevel);
        }
    }


}
