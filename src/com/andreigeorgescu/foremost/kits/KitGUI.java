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

    ItemStackCreator itemStackCreator;


    public KitGUI() {
        itemStackCreator = new ItemStackCreator();
    }

    public Inventory generateKitPreviewGUI(Player p, Kit kit) {
        Inventory kitPreviewInventory = Bukkit.createInventory(null, 54, "Kit Preview: " + kit.getName());
        ItemStack[] kitItems = kit.getKit();
        ItemStack[] bottomBar = getKitPreviewBottomBar(p, kit);
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

    public ItemStack[] getKitPreviewBottomBar(Player p, Kit kit) {
        ItemStack bottomBar[] = new ItemStack[9];

        for(int i = 0; i < 9; i++) {
            if(i == 3) {
                bottomBar[i] = itemStackCreator.createItemStack(
                        Material.PAPER,
                        ChatColor.LIGHT_PURPLE + "Kit Information",
                        Arrays.asList(
                                ChatColor.GRAY + "Cooldown: " + (kit.getCooldown() == -1 ? ChatColor.GREEN + "None" : ChatColor.GREEN.toString() + kit.getCooldownString()),
                                ChatColor.GRAY + "Unlocked: " + (kit.hasPermission(p) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"))
                );
            } else {
                bottomBar[i] = itemStackCreator.createPlaceholderItem();
            }
        }

        return bottomBar;
    }

}
