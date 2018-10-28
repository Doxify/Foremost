package com.andreigeorgescu.foremost;

import java.util.HashMap;
import java.util.List;

public class CooldownManager {

    private Foremost plugin;
    private HashMap<String, Cooldown> repairCooldown = new HashMap<>();
    private HashMap<String, Cooldown> repairAllCooldown = new HashMap<>();
    private HashMap<String, Cooldown> healCooldown = new HashMap<>();

    public CooldownManager(Foremost p) {
        plugin = p;
    }

    public void handleServerClose() {
        repairCooldown.clear();
        repairAllCooldown.clear();
        healCooldown.clear();
    }

    public void handleDisconnect(String uuid) {
        if(hasCooldown(uuid, "HEAL") && !hasCooldownExpired(uuid, "HEAL")) {
            healCooldown.remove(uuid);
        }

        if(hasCooldown(uuid, "REPAIR") && !hasCooldownExpired(uuid, "REPAIR")) {
            repairCooldown.remove(uuid);
        }

        if(hasCooldownExpired(uuid, "REPAIRALL") && !hasCooldownExpired(uuid, "REPAIRALL")) {
            repairAllCooldown.remove(uuid);
        }
    }

    public boolean hasCooldown(String uuid, String cooldownType) {
        switch(cooldownType.toUpperCase()) {
            case "HEAL": {
                if(healCooldown.containsKey(uuid)) {
                    return true;
                } else {
                    return false;
                }
            }

            case "REPAIR": {
                if(repairCooldown.containsKey(uuid)) {
                    return true;
                } else {
                    return false;
                }
            }

            case "REPAIRALL": {
                if(repairAllCooldown.containsKey(uuid)) {
                    return true;
                } else {
                    return false;
                }
            }

            default: {
                return false;
            }
        }
    }

    public boolean addCooldown(String uuid, String cooldownType, int cooldown) {
        switch(cooldownType.toUpperCase()) {
            case "HEAL": {
                Cooldown cd = new Cooldown((System.currentTimeMillis() / 1000), cooldown);
                healCooldown.put(uuid, cd);
                return true;
            }

            case "REPAIR": {
                Cooldown cd = new Cooldown((System.currentTimeMillis() / 1000), cooldown);
                repairCooldown.put(uuid, cd);
                return true;
            }

            case "REPAIRALL": {
                Cooldown cd = new Cooldown((System.currentTimeMillis() / 1000), cooldown);
                repairAllCooldown.put(uuid, cd);
                return true;
            }

            default: {
                return false;
            }
        }
    }

    public void deleteCooldown(String uuid, String cooldownType) {
        switch(cooldownType.toUpperCase()) {
            case "HEAL": {
                if(healCooldown.containsKey(uuid)) {
                    healCooldown.remove(uuid);
                }
                break;
            }

            case "REPAIR": {
                if(repairCooldown.containsKey(uuid)) {
                    repairCooldown.remove(uuid);
                }
                break;
            }

            case "REPAIRALL": {
                if(repairAllCooldown.containsKey(uuid)) {
                    repairAllCooldown.remove(uuid);
                }
                break;
            }

            default: {
                break;
            }
        }
    }

    public boolean hasCooldownExpired(String uuid, String cooldownType) {
        switch(cooldownType.toUpperCase()) {
            case "HEAL": {
                if(healCooldown.containsKey(uuid)) {
                    return healCooldown.get(uuid).cooldownExpired();
                } else {
                    return false;
                }
            }

            case "REPAIR": {
                if(repairCooldown.containsKey(uuid)) {
                    return repairCooldown.get(uuid).cooldownExpired();
                } else {
                    return false;
                }
            }

            case "REPAIRALL": {
                if(repairAllCooldown.containsKey(uuid)) {
                    return repairAllCooldown.get(uuid).cooldownExpired();
                } else {
                    return false;
                }
            }

            default: {
                return true;
            }
        }
    }

    public int getRemainingCooldown(String uuid, String cooldownType) {
        switch(cooldownType.toUpperCase()) {
            case "HEAL": {
                if(healCooldown.containsKey(uuid)) {
                    return healCooldown.get(uuid).getRemainingCooldown();
                } else {
                    return 0;
                }
            }

            case "REPAIR": {
                if(repairCooldown.containsKey(uuid)) {
                    return repairCooldown.get(uuid).getRemainingCooldown();
                } else {
                    return 0;
                }
            }

            case "REPAIRALL": {
                if(repairAllCooldown.containsKey(uuid)) {
                    return repairAllCooldown.get(uuid).getRemainingCooldown();
                } else {
                    return 0;
                }
            }

            default: {
                return -1;
            }
        }
    }
}

























//    // ---------------------------------
//    // healCooldown Methods
//    // ---------------------------------
//    public boolean hasHealCooldown(String uuid) {
//        if(healCooldown.containsKey(uuid)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean healCooldownExpired(String uuid) {
//        if(hasHealCooldown(uuid)) {
//            if(healCooldown.get(uuid).cooldownExpired()) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    public int getHealCooldown(String uuid) {
//        if(hasHealCooldown(uuid)) {
//            return healCooldown.get(uuid).getRemainingCooldown();
//        } else {
//            return 0;
//        }
//    }
//
//    public void addHealCooldown(String uuid, int cooldown) {
//        healCooldown.put(uuid, new Cooldown(System.currentTimeMillis() / 1000, cooldown));
//    }
//
//    public void removeHealCooldown(String uuid) {
//        healCooldown.remove(uuid);
//    }
//
//
//    // ---------------------------------
//    // repairCooldown Methods
//    // ---------------------------------
//    public boolean hasRepairCooldown(String uuid) {
//        if(repairCooldown.containsKey(uuid)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean hasRepairCooldownExpired(String uuid) {
//        if(hasRepairCooldown(uuid)) {
//            if(repairCooldown.get(uuid).cooldownExpired()) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    public int getRepairCooldown(String uuid)
//
//
//
//    public long getRepairCooldown(String id, int cooldown) {
//        if(inRepairCooldown(id)) {
//            if((repairCooldown.get(id) + cooldown) >= (System.currentTimeMillis() / 1000)) {
//                // Cooldown isn't over!
//                return ((repairCooldown.get(id) + cooldown) - (System.currentTimeMillis() / 1000));
//            } else {
//                // Cooldown is over
//                repairCooldown.remove(id);
//                return 0; //
//            }
//        } else {
//            return -1;
//        }
//    }
//
//    public void setRepairCooldown(String id) {
//        repairCooldown.put(id, (System.currentTimeMillis() / 1000));
//    }
//
//    // ---------------------------------
//    // repairAllCooldown Methods
//    // ---------------------------------
//    public boolean inRepairAllCooldown(String id) {
//        if(repairAllCooldown.containsKey(id)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public long getRepairAllCooldown(String id, int cooldown) {
//        if(inRepairAllCooldown(id)) {
//            if((repairAllCooldown.get(id) + cooldown) >= (System.currentTimeMillis() / 1000)) {
//                // Cooldown isn't over!
//                return ((repairAllCooldown.get(id) + cooldown) - (System.currentTimeMillis() / 1000));
//            } else {
//                // Cooldown is over
//                repairAllCooldown.remove(id);
//                return 0; //
//            }
//        } else {
//            return -1;
//        }
//    }
//
//    public void setRepairAllCooldown(String id) {
//        repairAllCooldown.put(id, (System.currentTimeMillis() / 1000));
//    }
//}
