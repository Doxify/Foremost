package com.andreigeorgescu.foremost.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
//        int kitItems = 0;
//
//        for(ItemStack item : k) {
//            if(item != null) {
//                kitItems++;
//            }
//        }
//
//        kit = new ItemStack[kitItems];
//
//        for(int i = 0; i < kitItems; i++) {
//            kit[i] = new ItemStack(k[i]);
//        }

        List<ItemStack> kitItems = new ArrayList<>();

        for(int i = 0; i < k.length; i++) {
            if(k[i] != null) {
                if(k[i].getType() != Material.AIR) {
                    kitItems.add(k[i].clone());
                }
            }
        }

        kit = new ItemStack[kitItems.size()];
        for(int i = 0; i < kitItems.size(); i++) {
            kit[i] = kitItems.get(i);
        }

    }


    public boolean hasPermission(Player p) {
        if(p.hasPermission("foremost.kit." + name.toLowerCase())) {
            return true;
        }
        return false;
    }

    public String getCooldownString() {
        StringBuilder cooldownString = new StringBuilder();
        int milliseconds = cooldown * 1000;
        int hours   = ((milliseconds / (1000*60*60)) % 24);
        int minutes = ((milliseconds / (1000*60)) % 60);
        int seconds = (milliseconds / 1000) % 60 ;

        cooldownString.append(hours < 1 ? "00" : (hours < 10 ? "0" + hours : Integer.toString(hours)));
        cooldownString.append(":");
        cooldownString.append(minutes < 1 ? "00" : minutes < 10 ? "0" + minutes : Integer.toString(minutes));
        cooldownString.append(":");
        cooldownString.append(seconds < 10 ? "0" + seconds : seconds);

        return cooldownString.toString();
    }

}
