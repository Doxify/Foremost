package com.andreigeorgescu.foremost;

import java.util.HashMap;

public class CooldownManager {

    private Foremost plugin;
    private HashMap<String, Long> repairCooldown = new HashMap<>();
    private HashMap<String, Long> repairAllCooldown = new HashMap<>();
    private HashMap<String, Long> healCooldown = new HashMap<>();

    public CooldownManager(Foremost p) {
        plugin = p;
    }


    // ---------------------------------
    // Cooldown Manager Methods
    // ---------------------------------
    public void handleDisconnect(String id) {
        if(inRepairAllCooldown(id))
            repairAllCooldown.remove(id);

        if(inRepairCooldown(id)) {
            repairCooldown.remove(id);
        }
    }

    // ---------------------------------
    // healCooldown Methods
    // ---------------------------------
    public boolean inHealCooldown(String id) {
        if(healCooldown.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }

    public long getHealCooldown(String id, int cooldown) {
        if(inHealCooldown(id)) {
            if((healCooldown.get(id) + cooldown) >= (System.currentTimeMillis() / 1000)) {
                // Cooldown isn't over!
                return ((healCooldown.get(id) + cooldown) - (System.currentTimeMillis() / 1000));
            } else {
                // Cooldown is over
                healCooldown.remove(id);
                return 0; //
            }
        } else {
            return -1;
        }
    }

    public void setHealCooldown(String id) {
        healCooldown.put(id, (System.currentTimeMillis() / 1000));
    }


    // ---------------------------------
    // repairCooldown Methods
    // ---------------------------------
    public boolean inRepairCooldown(String id) {
        if(repairCooldown.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }

    public long getRepairCooldown(String id, int cooldown) {
        if(inRepairCooldown(id)) {
            if((repairCooldown.get(id) + cooldown) >= (System.currentTimeMillis() / 1000)) {
                // Cooldown isn't over!
                return ((repairCooldown.get(id) + cooldown) - (System.currentTimeMillis() / 1000));
            } else {
                // Cooldown is over
                repairCooldown.remove(id);
                return 0; //
            }
        } else {
            return -1;
        }
    }

    public void setRepairCooldown(String id) {
        repairCooldown.put(id, (System.currentTimeMillis() / 1000));
    }

    // ---------------------------------
    // repairAllCooldown Methods
    // ---------------------------------
    public boolean inRepairAllCooldown(String id) {
        if(repairAllCooldown.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }

    public long getRepairAllCooldown(String id, int cooldown) {
        if(inRepairAllCooldown(id)) {
            if((repairAllCooldown.get(id) + cooldown) >= (System.currentTimeMillis() / 1000)) {
                // Cooldown isn't over!
                return ((repairAllCooldown.get(id) + cooldown) - (System.currentTimeMillis() / 1000));
            } else {
                // Cooldown is over
                repairAllCooldown.remove(id);
                return 0; //
            }
        } else {
            return -1;
        }
    }

    public void setRepairAllCooldown(String id) {
        repairAllCooldown.put(id, (System.currentTimeMillis() / 1000));
    }
}
