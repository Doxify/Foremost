package com.andreigeorgescu.foremost.kits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class KitsManager {

    private static List<Kit> kits;
    private static HashMap<UUID, HashMap<String, String>> cooldowns;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
    private boolean enabled;

    public KitsManager(List<Kit> k, HashMap<UUID, HashMap<String, String>> c) {
        kits = k;
        cooldowns = c;
        removeExpiredCooldowns();

        if(kits.size() > 1) {
            enabled = true;
            System.out.println("[Foremost] Kits Manager has been enabled with " + kits.size() + " kits.");
        } else {
            enabled = false;
        }
    }

    public void handleServerClose() {
        kits.clear();
        cooldowns.clear();
    }

    public static List<Kit> getKits() {
        return kits;
    }

    public HashMap<UUID, HashMap<String, String>> getCooldowns() {
        return cooldowns;
    }

    /*
        Gets the status of the Kits Manager

        Returns true if 1 kit or more has been loaded
        Returns false if no kits have been loaded
         */
    public boolean isEnabled() {
        return enabled;
    }

    /*
    Gets a list of kits based on the player's
    permission.

    Parameters:
    - Player: the player who requested the list
     */
    public void getKitList(Player p) {
        boolean firstKit = true;
        StringBuilder kitList = new StringBuilder(ChatColor.GREEN + ChatColor.BOLD.toString() + "Kits\n");

        if(!isEnabled()) {
            p.sendMessage(ChatColor.RED + "There are currently no kits available.");
            return;
        }

        for(Kit kit : kits) {
            // Kit permission format: foremost.kit.kitName
            if(kit.hasPermission(p)) {
                if(firstKit) {
                    kitList.append(ChatColor.GREEN + kit.getName());
                    firstKit = false;
                } else {
                    kitList.append(ChatColor.GRAY + ", " + ChatColor.GREEN + kit.getName());
                }
            } else {
                if(kit.getCooldown() != -1) {
                    if(firstKit) {
                        kitList.append(ChatColor.RED + kit.getName());
                        firstKit = false;
                    } else {
                        kitList.append(ChatColor.GRAY + ", " + ChatColor.RED + kit.getName());
                    }
                }
            }
        }
        p.sendMessage(kitList.toString());
    }

    /*
    Checks if a kit exists in the kits list

    Parameters:
    - String: kit's name

    Returns true if it does
    Returns false if it doesn't
     */
    public boolean kitExists(String name) {
        if(isEnabled()) {
            for(Kit kit : kits) {
                if(kit.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    Gets a Kit object that has the same name as
    the String in the parameter

    Parameters:
    - String: kit's name

    Returns Kit if the kit is found
    Returns NULL if the kit isn't found
     */
    public Kit getKitObject(String name) {
        if(isEnabled()) {
            if(kitExists(name)) {
                for(Kit kit : kits) {
                    if(kit.getName().equalsIgnoreCase(name)) {
                        return kit;
                    }
                }
            }
        }
        return null;
    }

    /*
    This will add a new kit into the kits list
    if one with the same name doesn't exist.

    Parameters:
    - String: kit's name
    - ItemStack[]: kit's items
    - Int: kit's cooldown in seconds

    Returns true if successful
    Returns false if unsuccessful
     */
    public boolean addKit(String name, int cooldown, ItemStack[] items) {
        if(kitExists(name)) {
            return false;
        }

        Kit Kit = new Kit(name, cooldown, items);
        kits.add(Kit);

        if(!enabled) {
            enabled = true;
        }

        return true;
    }

    /*
    This will delete a kit from the kits list
    if it exists.

    Parameters:
    - String: kit's name

    Returns true if successful
    Returns false if unsuccessful
     */
    public boolean deleteKit(String name) {
        if(kitExists(name)) {
            kits.remove(getKitObject(name));

            if(kits.size() < 1) {
                enabled = false;
            }
            return true;
        }
        return false;
    }

    /*
    Adds a kit cooldown into the cooldowns
    list.

    Parameters:
    - UUID: player's uuid
    - String: kit's name
    - int: kit's cooldown
     */
    public void setCooldown(UUID uuid, String kitName, int kitCooldown) {
        HashMap<String, String> playerCooldowns;
        LocalDateTime cooldown = LocalDateTime.now().plusSeconds(kitCooldown);

        if(cooldowns.containsKey(uuid)) {
            playerCooldowns = cooldowns.get(uuid);
            playerCooldowns.put(kitName, dtf.format(cooldown));
            cooldowns.put(uuid, playerCooldowns);
        } else {
            playerCooldowns = new HashMap<>();
            playerCooldowns.put(kitName, dtf.format(cooldown));
            cooldowns.put(uuid, playerCooldowns);
        }

    }

    /*
    Checks if a player has an active cooldown

    Parameters:
    - UUID: player's uuid
    - String: kit's name

    Returns true if they do
    Returns false if they don't
     */
    public static boolean hasCooldown(UUID uuid, String kitName) {
        if(!cooldowns.containsKey(uuid)) {
            return false;
        } else {
            HashMap<String, String> playerCooldowns = cooldowns.get(uuid);
            if(!playerCooldowns.containsKey(kitName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /*
    Checks if a player's cooldown is over

    Parameters:
    - UUID: player's uuid
    - String: kit's name

    Returns true if the cooldown is over
    Returns false if the cooldown isn't over
     */
    public static boolean isCooldownOver(UUID uuid, String kitName) {
        if(!hasCooldown(uuid, kitName)) {
            return true;
        } else {
            String cooldownString = cooldowns.get(uuid).get(kitName);
            LocalDateTime cooldown = LocalDateTime.parse(cooldownString, dtf);
            LocalDateTime current = LocalDateTime.now();
            int result = current.compareTo(cooldown);

            if(result == 0) {
                return false;
            } else if(result < 0) {
                return false;
            } else {
                return true;
            }

        }
    }

    /*
    Removes a cooldown from a player's
    cooldown map and removes their uuid from
    the map if they have no other cooldowns.

    Parameters:
    - UUID: player's uuid
    - String: kit's name
     */
    public void removeCooldown(UUID uuid, String kitName) {
        if(hasCooldown(uuid, kitName)) {
            cooldowns.get(uuid).remove(kitName);

            // Checking for other cooldowns
            HashMap<String, String> playerCooldowns = cooldowns.get(uuid);
            if(playerCooldowns.isEmpty()) {
                cooldowns.remove(uuid);
            }
        }
    }

    /*
    Gets the player's kit cooldown in the form
    of a calendar object

    Parameters:
    - UUID: player's uuid
    - String: kit's name

    Returns a DateTime object if there's a cooldown
    Returns null if the cooldown is over / doesn't exist.
     */
    public static LocalDateTime getCooldown(UUID uuid, String kitName) {
        if(!hasCooldown(uuid, kitName)) {
            return null;
        } else {
            if(isCooldownOver(uuid, kitName)) {
               return null;
            } else {
                String cooldownString = cooldowns.get(uuid).get(kitName);
                LocalDateTime cooldown = LocalDateTime.parse(cooldownString, dtf);
                return cooldown;
            }
        }
    }

    /*
    Removes all cooldowns that have expired
    from the cooldowns map
     */
    public void removeExpiredCooldowns() {
        for(UUID uuid : cooldowns.keySet()) {
            if(cooldowns.get(uuid).isEmpty()) {
                cooldowns.remove(uuid);
            } else {
                for(String kitName : cooldowns.get(uuid).keySet()) {
                    if(isCooldownOver(uuid, kitName)) {
                        removeCooldown(uuid, kitName);
                    }
                }
            }
        }
    }

    /*
    Returns a cooldown in a nicely formatted string

    Parameters:
    - LocalDateTime: the cooldown

    Returns a string
     */
    public static String getCooldownString(LocalDateTime cooldown) {
        StringBuilder cooldownString = new StringBuilder();
        LocalDateTime currentTime = LocalDateTime.now();
        long milliseconds = Duration.between(currentTime, cooldown).toMillis();
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int seconds = (int) (milliseconds / 1000) % 60 ;

        cooldownString.append(hours < 1 ? "00" : (hours < 10 ? "0" + hours : Integer.toString(hours)));
        cooldownString.append(":");
        cooldownString.append(minutes < 1 ? "00" : minutes < 10 ? "0" + minutes : Integer.toString(minutes));
        cooldownString.append(":");
        cooldownString.append(seconds < 10 ? "0" + seconds : seconds);

        return cooldownString.toString();
    }

    /*
    Gives the player a kit and handles whether
    or not they are given a cooldown

    Parameters:
    - Player: The player who receives the kit
    - String: The name of the kit

     */
    public void givePlayerKit(Player p, Kit kit) {
        ItemStack kitItems[] = kit.getKit().clone();
        HashMap<Integer, ItemStack> items = p.getInventory().addItem(kitItems);

        if (items.size() > 0) {
            for (ItemStack item : items.values()) {
                p.getWorld().dropItem(p.getLocation(), item);
            }
        }

        p.updateInventory();

        if(kit.getCooldown() != -1) {
            if(!p.hasPermission("foremost.kit.bypass")) {
                setCooldown(p.getUniqueId(), kit.getName(), kit.getCooldown());
                p.sendMessage(ChatColor.GREEN + "Successfully redeemed kit: " + kit.getName());
                p.sendMessage(ChatColor.YELLOW + "Cooldown: " + getCooldownString(getCooldown(p.getUniqueId(), kit.getName())));
            } else {
                p.sendMessage(ChatColor.GREEN + "Successfully redeemed kit: " + kit.getName());
            }
        }
    }

}
