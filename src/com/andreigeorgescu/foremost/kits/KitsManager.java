package com.andreigeorgescu.foremost.kits;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class KitsManager {

    private List<Kit> kits;
    private HashMap<UUID, HashMap<String, String>> cooldowns;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);


    private boolean enabled;


    public KitsManager(List<Kit> k, HashMap<UUID, HashMap<String, String>> c) {
        kits = k;
        cooldowns = c;

        if(kits.size() > 1) {
            enabled = true;
            System.out.println("[Foremost] Kits Manager has been enabled with " + kits.size() + " kits.");
        } else {
            enabled = false;
        }
    }

    public KitsManager() {
        kits = new ArrayList<>();
        cooldowns = new HashMap<>();
        enabled = false;
        System.out.println("[Foremost] Kits Manager has been enabled with no kits or cooldowns.");
    }

    public void handleServerClose() {
        kits.clear();
        cooldowns.clear();
    }

    public List<Kit> getKits() {
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
        StringBuilder kitList = new StringBuilder(org.bukkit.ChatColor.GREEN + org.bukkit.ChatColor.BOLD.toString() + "Kits\n");


        if(!isEnabled()) {
            p.sendMessage(ChatColor.RED + "There are currently no kits available.");
            return;
        }

        for(Kit kit : kits) {
            // Kit permission format: foremost.kit.kitName
            if(kit.hasPermission(p)) {
                if(firstKit) {
                    kitList.append(org.bukkit.ChatColor.GREEN + kit.getName());
                    firstKit = false;
                } else {
                    kitList.append(org.bukkit.ChatColor.GRAY + ", " + org.bukkit.ChatColor.GREEN + kit.getName());
                }
            } else {
                if(firstKit) {
                    kitList.append(org.bukkit.ChatColor.RED + kit.getName());
                    firstKit = false;
                } else {
                    kitList.append(org.bukkit.ChatColor.GRAY + ", " + org.bukkit.ChatColor.RED + kit.getName());
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
    - Kit: kit object
     */
    public void setCooldown(UUID uuid, Kit kit) {
        HashMap<String, String> playerCooldowns;
        LocalDateTime cooldown = LocalDateTime.now().plusSeconds(kit.getCooldown());

        if(cooldowns.containsKey(uuid)) {
            playerCooldowns = cooldowns.get(uuid);
            playerCooldowns.put(kit.getName(), dtf.format(cooldown));
            cooldowns.put(uuid, playerCooldowns);
        } else {
            playerCooldowns = new HashMap<>();
            playerCooldowns.put(kit.getName(), dtf.format(cooldown));
            cooldowns.put(uuid, playerCooldowns);
        }

    }

    /*
    Checks if a player has an active cooldown

    Parameters:
    - UUID: player's uuid
    - Kit: kit object

    Returns true if they do
    Returns false if they don't
     */
    public boolean hasCooldown(UUID uuid, Kit kit) {
        if(!cooldowns.containsKey(uuid)) {
            return false;
        } else {
            HashMap<String, String> playerCooldowns = cooldowns.get(uuid);
            if(!playerCooldowns.containsKey(kit.getName())) {
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
    - Kit: kit object

    Returns true if the cooldown is over and
    removes it from the map

    Returns false if the cooldown isn't over
     */
    public boolean isCooldownOver(UUID uuid, Kit kit) {
        if(!hasCooldown(uuid, kit)) {
            return true;
        } else {
            String cooldownString = cooldowns.get(uuid).get(kit.getName());
            LocalDateTime cooldown = LocalDateTime.parse(cooldownString, dtf);
            LocalDateTime current = LocalDateTime.now();
            int result = current.compareTo(cooldown);

            if(result == 0) {
                return false;
            } else if(result < 0) {
                return false;
            } else {
                removeCooldown(uuid, kit);
                return true;
            }

        }
    }

    /*
    Removes a cooldown from a player's
    cooldown map

    Parameters:
    - UUID: player's uuid
    - Kit: kit object
     */
    public void removeCooldown(UUID uuid, Kit kit) {
        if(hasCooldown(uuid, kit)) {
            cooldowns.get(uuid).remove(kit.getName());
        }
    }

    /*
    Gets the player's kit cooldown in the form
    of a calendar object

    Parameters:
    - UUID: player's uuid
    - Kit: kit object

    Returns a DateTime object if there's a cooldown
    Returns null if the cooldown is over / doesn't exist.
     */
    public LocalDateTime getCooldown(UUID uuid, Kit kit) {
        if(!hasCooldown(uuid, kit)) {
            return null;
        } else {
            if(isCooldownOver(uuid, kit)) {
               return null;
            } else {
                String cooldownString = cooldowns.get(uuid).get(kit.getName());
                LocalDateTime cooldown = LocalDateTime.parse(cooldownString, dtf);
                return cooldown;
            }
        }
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
        p.sendMessage(org.bukkit.ChatColor.GREEN + "Successfully redeemed kit: " + kit.getName());
//        if(!p.hasPermission("foremost.kit.bypass")) {
//            setCooldown(p.getUniqueId(), kit);
//            p.sendMessage("DEBUG: Cooldown expires: " + getCooldown(p.getUniqueId(), kit).toString());
//        }
        setCooldown(p.getUniqueId(), kit);
        p.sendMessage("DEBUG: Cooldown expires: " + getCooldown(p.getUniqueId(), kit));

    }
}
