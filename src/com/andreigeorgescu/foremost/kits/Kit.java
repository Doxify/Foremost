package com.andreigeorgescu.foremost.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {

    private String name;
    private int cooldown;
    private ItemStack[] kit;

    public Kit(String n, int c, ItemStack[] k) {
        setName(n);
        setCooldown(c);
        setKit(k);
    }

    public String getName() { return name; }
    public int getCooldown() { return cooldown; }
    public ItemStack[] getKit() { return kit; }

    public void setName(String n) {
        name = n;
    }

    public void setCooldown(int c) {
        cooldown = c;
    }

    public void setKit(ItemStack[] k) {
        int kitItems = 0;

        for(ItemStack item : k) {
            if(item != null) {
                kitItems++;
            }
        }

        kit = new ItemStack[kitItems];

        for(int i = 0; i < kitItems; i++) {
            kit[i] = new ItemStack(k[i]);
        }

    }

    public boolean hasPermission(Player p) {
        if(p.hasPermission("foremost.kit." + name.toLowerCase())) {
            return true;
        }
        return false;
    }

}
