//package com.andreigeorgescu.foremost;
//
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.inventory.meta.SkullMeta;
//
//import java.util.*;
//
//public class StaffModeManager {
//
//    Foremost plugin;
//
//    public StaffModeManager(Foremost p) {
//        plugin = p;
//    }
//
//    private HashMap<String, StaffMode> staff = new HashMap<>();
//
//    public Set<UUID> getStaffUUIDS() {
//        Set<String> stringUUIDS = staff.keySet();
//        Set<UUID> formattedUUIDS = new HashSet<>();
//        for(String staffMemberUUID : stringUUIDS) {
//            UUID formattedUUID = staff.get(staffMemberUUID).getPlayerUUID();
//            formattedUUIDS.add(formattedUUID);
//        }
//
//        return formattedUUIDS;
//    }
//
//    public boolean hasStaffMode(String uuid) {
//        if(staff.containsKey(uuid)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void addStaff(String uuid) {
//        if(!hasStaffMode(uuid)) {
//            staff.put(uuid, new StaffMode(uuid));
//
//            // TODO: Implement vanish
////            // Making sure all players who are in staff mode are vanished.
////            if(!plugin.nsaPlugin.getProfileManager().getProfile(uuid).getVanish()) {
////                Player p = Bukkit.getPlayer(staff.get(uuid).getPlayerUUID());
////                if(p != null) {
////                    p.performCommand("vanish");
////                }
////            }
//
//        }
//    }
//
//    public void removeStaff(String uuid) {
//        if(hasStaffMode(uuid)) {
//            StaffMode staffMode = staff.get(uuid);
//            Player p = Bukkit.getPlayer(staffMode.getPlayerUUID());
//            staffMode.quitStaffMode();
//            staff.remove(uuid);
//
//        }
//    }
//
//    public StaffMode getStaffMode(String uuid) {
//        if(hasStaffMode(uuid)) {
//            return staff.get(uuid);
//        } else {
//            return null;
//        }
//    }
//
//    public void handleServerClose() {
//        for(String uuid : staff.keySet()) {
//            removeStaff(uuid);
//        }
//    }
//
//    // ------------------ Staff Tool Methods ------------------- //
//
//    // --------------------------------------------------------- //
//    //  Function: handleRandomTeleport()
//    //  Parameters: Player
//    //  Returns: UUID
//    //  Description: Teleports target to a random online player
//    //               and returns the UUID of the player.
//    // --------------------------------------------------------- //
//    public UUID handleRandomTeleport(Player p) {
//        if(p != null) {
//            ArrayList<UUID> targets = new ArrayList<>();
//
//            for(Player target : Bukkit.getServer().getOnlinePlayers()) {
//                if(target != p) {
//                    targets.add(target.getUniqueId());
//                }
//            }
//
//            int onlinePlayers = targets.size();
//            if(onlinePlayers > 0) {
//                int randomNumber = generateRandomNumber(0, onlinePlayers);
//                return targets.get(randomNumber);
//            } else {
//                return null;
//            }
//        } else {
//            return null;
//        }
//    }
//
//    // --------------------------------------------------------- //
//    //  Function: getServerToolsGui()
//    //  Parameters: Player
//    //  Returns: Inventory
//    //  Description: Gets a list of all server settings and displays
//    //               it nicely in a GUI.
//    // --------------------------------------------------------- //
//    public Inventory getServerToolsGui(Player p) {
//        int inventorySize = 9;
//        Inventory inv = Bukkit.createInventory(null, inventorySize, "Server Tools");
//        boolean chatMuted = plugin.chatManager.getChatMuteSetting();
//
//        inv.addItem(createServerToolsGuiItem(
//            (chatMuted ? ChatColor.GREEN + "Unmute Chat" : ChatColor.RED + "Mute Chat"),
//            (chatMuted ? new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Click to unmute chat", "", ChatColor.GRAY + "/chat unmute")) : new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Click to mute chat", "", ChatColor.GRAY + "/chat mute"))),
//            (chatMuted ? Material.GLOWSTONE_DUST : Material.REDSTONE)
//        ));
//
//        inv.addItem(createServerToolsGuiItem(
//            ChatColor.RED + "Clear Chat",
//            new ArrayList<String>(Arrays.asList(ChatColor.GRAY + "Click to clear chat", "", ChatColor.GRAY + "/chat clear")),
//            Material.COMMAND
//        ));
//
//        return inv;
//    }
//
//    // --------------------------------------------------------- //
//    //  Function: createServerToolsGuiItem()
//    //  Parameters:
//    //  Returns: ItemStack
//    //  Description:
//    //
//    // --------------------------------------------------------- //
//    public ItemStack createServerToolsGuiItem(String name, List<String> lore, Material guiMaterial) {
//        ItemStack item = new ItemStack(guiMaterial, 1);
//        ItemMeta itemMeta = item.getItemMeta();
//        itemMeta.setDisplayName(name);
//        itemMeta.setLore(lore);
//        item.setItemMeta(itemMeta);
//
//        return item;
//    }
//
//
//    // --------------------------------------------------------- //
//    //  Function: getOnlineStaffGUI()
//    //  Parameters: Player
//    //  Returns: Inventory
//    //  Description: Gets a list of all online staff and displays
//    //               it nicely in a GUI.
//    // --------------------------------------------------------- //
//    public Inventory getOnlineStaffGUI(Player p) {
//        Set<UUID> staff = getStaffUUIDS();
//
//        // Checking for staff who are online but not in staff mode.
//        for(Player target : Bukkit.getServer().getOnlinePlayers()) {
//            if((target != p) && (!staff.contains(target.getUniqueId().toString())) && (target.hasPermission("foremost.staffmode"))) {
//                staff.add(target.getUniqueId());
//            }
//        }
//
//        if(staff.size() > 1) {
//            int inventorySize = staff.size() + (9 - staff.size() % 9);
//            Inventory inv = Bukkit.createInventory(null, inventorySize, "Online Staff");
//
//            for(UUID staffMemberUUID : staff) {
//                inv.addItem(createOnlineStaffGuiItem(staffMemberUUID));
//            }
//
//            return inv;
//
//        } else {
//            p.sendMessage(ChatColor.RED + "You are the only staff member online on this server.");
//            return null;
//        }
//    }
//
//    // --------------------------------------------------------- //
//    //  Function: createOnlineStaffGuiItem()
//    //  Parameters: UUID
//    //  Returns: ItemStack
//    //  Description: Creates an ItemStack for on online staff
//    //               member.
//    // --------------------------------------------------------- //
//    public ItemStack createOnlineStaffGuiItem(UUID uuid) {
//        Player target = Bukkit.getPlayer(uuid);
//        if(target != null) {
//            ItemStack staffGuiItem = getHead(target);
//            SkullMeta staffGuiItemMeta = (SkullMeta) staffGuiItem.getItemMeta();
//            List<String> lore = new ArrayList<>(Arrays.asList(
//                ChatColor.WHITE + "Rank: " + ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(target)),
//                ChatColor.WHITE + "Staff Mode: " + (hasStaffMode(uuid.toString()) == true ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"),
//                " ",
//                ChatColor.YELLOW + "Click to teleport"
//            ));
//
//            staffGuiItemMeta.setDisplayName(ChatColor.GREEN + target.getName());
//            staffGuiItemMeta.setLore(lore);
//            staffGuiItem.setItemMeta(staffGuiItemMeta);
//
//            return staffGuiItem;
//        }
//
//        return null;
//    }
//
//    // --------------------------------------------------------- //
//    //  Function: generateRandomNumber(int min, int max)
//    //  Parameters: int, int
//    //  Returns: int
//    //  Description: Generates a random number between the min
//    //               and the max and returns it.
//    // --------------------------------------------------------- //
//    public static int generateRandomNumber(int min, int max) {
//        Random random = new Random();
//        return random.nextInt((max - min) + min);
//    }
//
//    // --------------------------------------------------------- //
//    //  Function: getHead(Player p)
//    //  Parameters: Player
//    //  Returns: ItemStack
//    //  Description: Creates an ItemStack of the head of the
//    //               Player parameter and returns it.
//    // --------------------------------------------------------- //
//    public static ItemStack getHead(Player p) {
//        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
//        SkullMeta skull = (SkullMeta) item.getItemMeta();
//        skull.setDisplayName(ChatColor.GREEN + p.getName());
//        skull.setOwner(p.getName());
//        item.setItemMeta(skull);
//        return item;
//    }
//}
