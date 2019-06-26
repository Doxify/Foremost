package com.andreigeorgescu.foremost.cooldowns;

public class Cooldown {

    private Long timestamp;
    private int cooldown;

    public Cooldown(Long timestamp, int cooldown) {
        setTimestamp(timestamp);
        setCooldown(cooldown);
    }

    private void setTimestamp(Long t) {
        this.timestamp = t;
    }
    private void setCooldown(int c) {
        this.cooldown = c;
    }

    public Long getTimestamp() { return this.timestamp; }

    public boolean cooldownExpired() {
        if((this.timestamp + this.cooldown) <= (System.currentTimeMillis() / 1000)) {
            return true;
        } else {
            return false;
        }
    }

    public int getRemainingCooldown() {
        return (int) ((this.timestamp + this.cooldown) - (System.currentTimeMillis() / 1000));
    }

    public String toString() {
        return "Cooldown: " + cooldown + " seconds from " + this.timestamp;
    }
}
