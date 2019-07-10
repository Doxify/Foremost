package com.andreigeorgescu.foremost.staff;

import com.andreigeorgescu.foremost.Foremost;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.saphron.nsa.NSA;
import com.saphron.nsa.Utilities;
import net.minecraft.util.com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class StaffManager {

    private HashMap<UUID, StaffModePlayer> staff;
    private List<StaffModePlayer> vanished;
    private Inventory staffInventory;
    private Inventory togglesInventory;
    private int staffTaskID;

    private final ItemStack FUN_STICK = Utilities.createGuiItem(
            ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Fun Stick",
            new ArrayList<>(Arrays.asList(
                    ChatColor.AQUA + "Applies Jump Boost II for 2 seconds",
                    "",
                    ChatColor.GRAY + "Click a player to use"
            )),
            Material.BLAZE_ROD,
            0
    );

    private final ItemStack STAFF = Utilities.createGuiItem(
            ChatColor.RED + ChatColor.BOLD.toString() + "Staff",
            new ArrayList<>(Arrays.asList(
                    ChatColor.GRAY + "Right click to view Staff"
            )),
            Material.SKULL_ITEM,
            3
    );

    private final ItemStack THRU_ITEM = Utilities.createGuiItem(
            ChatColor.GREEN + ChatColor.BOLD.toString() + "Thru",
            new ArrayList<>(Arrays.asList(
                    ChatColor.GRAY + "Runs /thru"
            )),
            Material.CARROT_STICK,
            0
    );

    private final ItemStack VANISH = Utilities.createGuiItem(
            ChatColor.YELLOW + ChatColor.BOLD.toString() + "Vanish",
            new ArrayList<>(Arrays.asList(
                    ChatColor.GRAY + "Right click to toggle Vanish"
            )),
            Material.INK_SACK,
            8
    );

    private final ItemStack TOGGLES = Utilities.createGuiItem(
            ChatColor.RED + ChatColor.BOLD.toString() + "Toggles",
            new ArrayList<>(Arrays.asList(
                    ChatColor.GRAY + "Right click to open Toggles"

            )),
            Material.REDSTONE_COMPARATOR,
            0
    );

    private final ItemStack WORLD_EDIT = Utilities.createGuiItem(
            ChatColor.GOLD + ChatColor.BOLD.toString() + "World Edit",
            null,
            Material.WOOD_AXE,
            0
    );

    public StaffManager() {
        this.vanished = new ArrayList<>();
        this.staff = new HashMap<>();
        this.staffInventory = Bukkit.createInventory(null, 54, "Staff");
        this.togglesInventory = Bukkit.createInventory(null, 9, "Staff Toggles");
        startStaffTasks();
    }

    public void handleServerClose() {
        Bukkit.getScheduler().cancelTask(staffTaskID);
        for(UUID uuid : staff.keySet()) {
            StaffModePlayer staffModePlayer = staff.get(uuid);
            removeFromStaffMode(staffModePlayer.getPlayer());
        }

        staff.clear();
        vanished.clear();
    }

    // TASK
    public void startStaffTasks() {
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(Foremost.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if(staff.size() > 0) {
                    Collection<StaffModePlayer> staffModePlayers = staff.values();
                    StaffModePlayer staffModePlayer = (StaffModePlayer) staffModePlayers.toArray()[0];
                    Player player = staffModePlayer.getPlayer();

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Queued");
                    out.writeUTF(player.getUniqueId().toString());
                    out.writeUTF(Foremost.getPlugin().nsaPlugin.config.getServerName());
                    player.sendPluginMessage(Foremost.getPlugin(), "Queue", out.toByteArray());
                }
            }
        }, 0, 20 * 5);
        staffTaskID = task.getTaskId();
    }

    public boolean isStaffMode(Player player) {
        if(staff.containsKey(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    // Returns whether or not this player has the ability to use staff mode.
    public boolean hasStaffMode(Player player) {
        if(player.hasPermission("foremost.staff.auto")) {
            return true;
        } else if(player.hasPermission("foremost.staff")) {
            return true;
        }
        return false;
    }

    public void addToStaffMode(Player player) {
        StaffModePlayer staffModePlayer = new StaffModePlayer(player);
        staff.put(player.getUniqueId(), staffModePlayer);

        updateStaffMenu();
        giveStaffInventory(staffModePlayer);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendMessage(ChatColor.GREEN + "You are now in Staff mode!");

    }

    public void removeFromStaffMode(Player player) {
        StaffModePlayer staffModePlayer = staff.get(player.getUniqueId());
        if(staffModePlayer != null) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(staffModePlayer.getPreviousArmor());
            player.getInventory().setContents(staffModePlayer.getPreviousInventory());
            player.teleport(staffModePlayer.getPreviousLocation());
            player.setAllowFlight(false);
            player.setFlying(false);

            if(vanished.contains(staffModePlayer)) {
                removeVanished(staffModePlayer);
            }

            player.sendMessage(ChatColor.RED + "You are no longer in Staff mode!");
            staff.remove(player.getUniqueId());
            updateStaffMenu();
        }

    }

    public StaffModePlayer getStaffModePlayer(Player player) {
        return staff.get(player.getUniqueId());
    }

    public int getOnlineStaff() {
        int staff = 0;

        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(hasStaffMode(player)) {
                staff++;
            }
        }

        return staff;
    }

    public List<StaffModePlayer> getVanished() { return this.vanished; }

    public void addVanished(StaffModePlayer staffModePlayer) {
        staffModePlayer.setVanished(true);
        vanished.add(staffModePlayer);
        handleVanish(staffModePlayer);
        updateStaffMenu();
    }

    public void removeVanished(StaffModePlayer staffModePlayer) {
        staffModePlayer.setVanished(false);
        vanished.remove(staffModePlayer);
        handleVanish(staffModePlayer);
        updateStaffMenu();
    }

    public void handleVanish(StaffModePlayer staffModePlayer) {
        if(staffModePlayer.isVanished()) {
            for(Player target : Bukkit.getServer().getOnlinePlayers()) {
                if(!target.hasPermission("foremost.staff.auto") || !target.hasPermission("foremost.staff")) {
                    target.hidePlayer(staffModePlayer.getPlayer());
                }
            }
        } else {
            for(Player target : Bukkit.getServer().getOnlinePlayers()) {
                if(!target.hasPermission("foremost.staff.auto") || !target.hasPermission("foremost.staff")) {
                    target.showPlayer(staffModePlayer.getPlayer());
                }
            }
        }
    }


    public void giveStaffInventory(StaffModePlayer staffModePlayer) {
        Player p = staffModePlayer.getPlayer();

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        p.getInventory().setItem(0, FUN_STICK);
        p.getInventory().setItem(1, STAFF);
        p.getInventory().setItem(3, THRU_ITEM);
        p.getInventory().setItem(7, TOGGLES);
        p.getInventory().setItem(8, WORLD_EDIT);

        // Changing the color of the dye based on vanish status
        if(staffModePlayer.isVanished()) {
            ItemStack vanishItem = VANISH.clone();
            vanishItem.setDurability((short) 10);
            p.getInventory().setItem(2, vanishItem);
        } else {
            p.getInventory().setItem(2, VANISH);
        }

    }

    public Inventory getStaffMenu() {
        return staffInventory;
    }

    public void updateStaffMenu() {
        staffInventory.clear();

        int staffCounter = 0;
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(hasStaffMode(player)) {
                StaffModePlayer staffModePlayer = staff.get(player.getUniqueId());

                ItemStack staffItem = Utilities.createGuiItem(
                        ChatColor.translateAlternateColorCodes('&', Foremost.getPlugin().getChat().getPlayerPrefix(player)) + player.getName(),
                        new ArrayList<>(Arrays.asList(
                                ChatColor.GRAY + "Server: " + ChatColor.YELLOW + Foremost.getPlugin().nsaPlugin.config.getServerName(),
                                ChatColor.GRAY + "Staff Mode: " + (staffModePlayer == null ? ChatColor.RED + "False" : ChatColor.GREEN + "True"),
                                ChatColor.GRAY + "Vanished: " + (staffModePlayer == null ? ChatColor.RED +  "False" : (staffModePlayer.isVanished() ? ChatColor.GREEN + "True" : ChatColor.RED + "False")),
                                "",
                                ChatColor.YELLOW + "Click to teleport"
                        )),
                        Material.SKULL_ITEM,
                        3
                );

                SkullMeta skullMeta = (SkullMeta) staffItem.getItemMeta();
                skullMeta.setOwner(player.getName());
                staffItem.setItemMeta(skullMeta);
                this.staffInventory.setItem(staffCounter, staffItem);
                staffCounter++;
            }
        }
    }

    public Inventory getTogglesMenu() {  return togglesInventory; }

    public void updateTogglesMenu() {
        togglesInventory.clear();

        this.togglesInventory.setItem(0, Utilities.createGuiItem(
                ChatColor.AQUA + ChatColor.BOLD.toString() + "Mute Chat",
                new ArrayList<>(Arrays.asList(
                        ChatColor.RED + "Chat is currently " + (Foremost.getPlugin().chatManager.getChatMuteSetting() ? "" : "not ") + "muted.",
                        "",
                        ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + "Click to toggle chat mute"
                )),
                Material.INK_SACK,
                (Foremost.getPlugin().chatManager.getChatMuteSetting() ? 10 : 8)
        ));

        this.togglesInventory.setItem(1, Utilities.createGuiItem(
                ChatColor.AQUA + ChatColor.BOLD.toString() + "Clear Chat",
                new ArrayList<>(Arrays.asList(
                        ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + "Click to clear chat"
                )),
                Material.INK_SACK,
                9
        ));

        this.togglesInventory.setItem(2, Utilities.createGuiItem(
                ChatColor.AQUA + ChatColor.BOLD.toString() + "Slow Chat " + ChatColor.RED + "SOON",
                new ArrayList<>(Arrays.asList(
                        ChatColor.RED + "Chat is currently " + (Foremost.getPlugin().chatManager.getChatMuteSetting() ? "" : "not ") + "slowed.",
                        "",
                        ChatColor.YELLOW + ChatColor.UNDERLINE.toString() + "Click to toggle slow mute"
                )),
                Material.INK_SACK,
                (Foremost.getPlugin().chatManager.getChatMuteSetting() ? 10 : 8)
        ));


    }


}
