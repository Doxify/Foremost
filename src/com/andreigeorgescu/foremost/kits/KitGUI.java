package com.andreigeorgescu.foremost.kits;

import com.andreigeorgescu.foremost.utils.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class KitGUI {

    public Inventory generateKitPreviewGUI(Player p, Kit kit) {
        Inventory kitPreviewInventory = Bukkit.createInventory(null, 54, "Kit Preview: " + kit.getName());
        ItemStack[] kitItems = kit.getKit();
        ItemStack[] bottomBar = getKitPreviewBottomBar(p, kit, false);
        int bottomBarCounter = 0;

        for(int i = 0; i < kitItems.length; i++) {
            kitPreviewInventory.setItem(i, kitItems[i]);
        }

        for(int i = 45; i < 54; i++) {
            kitPreviewInventory.setItem(i, bottomBar[bottomBarCounter]);
            bottomBarCounter++;
        }

        return kitPreviewInventory;

    }

    public static Inventory getKitsInterface(Player p) {
        Inventory kitsInventory = Bukkit.createInventory(null, 54, "Kits");
        ItemStack[] bottomBar = getKitPreviewBottomBar(p, null, true);
        int bottomBarCounter = 0;
        int invCounter = 0;

        for(Kit kit : KitsManager.getKits()) {
            if(kit.getCooldown() != -1  || p.hasPermission("foremost.admin")) {
                ItemStack kitItem = ItemStackCreator.createItemStack(
                        (kit.hasPermission(p) ? Material.PAPER : Material.STAINED_GLASS_PANE),
                        ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + kit.getName(),
                        Arrays.asList(
                                ChatColor.GRAY + "Cooldown: " + (kit.getCooldown() == -1 ? ChatColor.GREEN + "None" : ChatColor.GREEN.toString() + kit.getCooldownString()),
                                ChatColor.GRAY + "Unlocked: " + (kit.hasPermission(p) ? ChatColor.GREEN + "True" : ChatColor.RED + "False"),
                                "",
                                (kit.hasPermission(p) ? (KitsManager.hasCooldown(p.getUniqueId(), kit.getName()) ? ChatColor.RED + "This kit is on cooldown" : ChatColor.YELLOW + "Click to redeem kit") : ChatColor.RED + "You don't have this kit unlocked"),
                                ChatColor.GRAY + "Right click to view kit contents.",
                                ChatColor.DARK_GRAY + ChatColor.ITALIC.toString() + "/kit preview " + kit.getName()
                        )
                );

                if(!kit.hasPermission(p)) {
                    kitItem.setDurability((short) 14);
                } else if(KitsManager.hasCooldown(p.getUniqueId(), kit.getName())) {
                    kitItem.setType(Material.STAINED_GLASS_PANE);
                    kitItem.setDurability((short) 4);
                }

                kitsInventory.setItem(invCounter, kitItem);
                invCounter++;
            }
        }

        for(int i = 45; i < 54; i++) {
            kitsInventory.setItem(i, bottomBar[bottomBarCounter]);
            bottomBarCounter++;
        }

        return kitsInventory;
    }

    public static ItemStack[] getKitPreviewBottomBar(Player p, Kit kit, boolean isMainMenu) {
        ItemStack bottomBar[] = new ItemStack[9];

        for(int i = 0; i < 9; i++) {
            if(i == 1 && !isMainMenu) {
                ItemStack item = ItemStackCreator.createItemStack(
                        Material.STAINED_GLASS_PANE,
                        ChatColor.GOLD + ChatColor.BOLD.toString() + "View All",
                        Arrays.asList(ChatColor.GRAY + "Click to view all kits")
                );
                item.setDurability((short) 1);
                bottomBar[i] = item;
            } else if(i == 4) {
                if(isMainMenu){
                    ItemStack item = ItemStackCreator.createItemStack(
                        Material.STAINED_GLASS_PANE,
                        ChatColor.RED + ChatColor.BOLD.toString() + "Exit",
                        Arrays.asList(ChatColor.GRAY + "Click to exit kits")
                    );
                    item.setDurability((short) 14);
                    bottomBar[i] = item;
                } else {
                    ItemStack item = ItemStackCreator.createItemStack(
                        Material.STAINED_GLASS_PANE,
                        ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + kit.getName(),
                        Arrays.asList(
                                ChatColor.GRAY + "Cooldown: " + (kit.getCooldown() == -1 ? ChatColor.GREEN + "None" : ChatColor.GREEN.toString() + kit.getCooldownString()),
                                ChatColor.GRAY + "Unlocked: " + (kit.hasPermission(p) ? ChatColor.GREEN + "True" : ChatColor.RED + "False"),
                                "",
                                (kit.hasPermission(p) ? (KitsManager.hasCooldown(p.getUniqueId(), kit.getName()) ? ChatColor.RED + "This kit is on cooldown" : ChatColor.YELLOW + "Click to redeem kit") : ChatColor.RED + "You don't have this kit unlocked")
                        )
                    );

                    if(KitsManager.hasCooldown(p.getUniqueId(), kit.getName())) {
                        item.setType(Material.STAINED_GLASS_PANE);
                        item.setDurability((short) 4);
                    } else {
                        item.setDurability((short) 5);

                    }

                    bottomBar[i] = item;
                }
            } else if(i == 7 && !isMainMenu) {
                ItemStack item = ItemStackCreator.createItemStack(
                        Material.STAINED_GLASS_PANE,
                        ChatColor.RED + ChatColor.BOLD.toString() + "Exit",
                        Arrays.asList(ChatColor.GRAY + "Click to exit kits")
                );
                item.setDurability((short) 14);
                bottomBar[i] = item;
            } else {
                bottomBar[i] = ItemStackCreator.createPlaceholderItem();
            }
        }

        return bottomBar;
    }

}
