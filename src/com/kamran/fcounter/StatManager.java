package com.kamran.fcounter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

public class StatManager {

    private static final FCounter plugin = FCounter.getPlugin();

    private static HashMap<UUID, Integer> fsGiven = new HashMap<>();
    private static HashMap<UUID, Integer> fsReceived = new HashMap<>();

    public static void loadStats() {
        if (plugin.getConfig().getConfigurationSection("stats.fsGiven") != null) {
            for (String uuid : plugin.getConfig().getConfigurationSection("stats.fsGiven").getKeys(false)) {
                if (Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
                    fsGiven.put(UUID.fromString(uuid), plugin.getConfig().getInt("stats.fsGiven." + uuid));
                }
            }
        }
        if (plugin.getConfig().getConfigurationSection("stats.fsReceived") != null) {
            for (String uuid : plugin.getConfig().getConfigurationSection("stats.fsReceived").getKeys(false)) {
                if (Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
                    fsReceived.put(UUID.fromString(uuid), plugin.getConfig().getInt("stats.fsReceived." + uuid));
                }
            }
        }
    }

    public static void saveStats() {
        for (UUID uuid : fsGiven.keySet()) {
            plugin.getConfig().set("stats.fsGiven." + uuid, fsGiven.get(uuid));
        }
        for (UUID uuid : fsReceived.keySet()) {
            plugin.getConfig().set("stats.fsReceived." + uuid, fsReceived.get(uuid));
        }
        plugin.saveConfig();
    }

    public static void loadIndividualStats(OfflinePlayer player) {
        if (player.hasPlayedBefore()) {
            UUID uuid = player.getUniqueId();
            fsGiven.put(uuid, plugin.getConfig().getInt("stats.fsGiven." + uuid));
            fsReceived.put(uuid, plugin.getConfig().getInt("stats.fsReceived." + uuid));
        }
    }

    public static void saveIndividualStats(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            plugin.getConfig().set("stats.fsGiven." + uuid, fsGiven.get(uuid));
            plugin.getConfig().set("stats.fsReceived." + uuid, fsReceived.get(uuid));
            plugin.saveConfig();
            fsGiven.remove(uuid);
            fsReceived.remove(uuid);
        }
    }

    public static void incrementFsGiven(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            if (fsGiven.containsKey(uuid)) {
                fsGiven.put(uuid, fsGiven.get(uuid)+1);
            } else {
                int old = plugin.getConfig().getInt("stats.fsGiven." + uuid);
                plugin.getConfig().set("stats.fsGiven." + uuid, old+1);
                plugin.saveConfig();
            }
        }
    }

    public static void incrementFsReceived(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            if (fsReceived.containsKey(uuid)) {
                fsReceived.put(uuid, fsReceived.get(uuid)+1);
            } else {
                int old = plugin.getConfig().getInt("stats.fsReceived." + uuid);
                plugin.getConfig().set("stats.fsReceived." + uuid, old+1);
                plugin.saveConfig();
            }
        }
    }

    public static void resetFsGiven(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            if (fsGiven.containsKey(uuid)) {
                fsGiven.put(uuid, 0);
            } else {
                plugin.getConfig().set("stats.fsGiven." + uuid, 0);
                plugin.saveConfig();
            }
        }
    }

    public static void resetFsReceived(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            if (fsReceived.containsKey(uuid)) {
                fsReceived.put(uuid, 0);
            } else {
                plugin.getConfig().set("stats.fsReceived." + uuid, 0);
                plugin.saveConfig();
            }
        }
    }

    public static int getFsGiven(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            if (fsGiven.containsKey(uuid)) {
                return fsGiven.get(uuid);
            }
            return plugin.getConfig().getInt("stats.fsGiven." + uuid);
        }
        return 0;
    }

    public static int getFsReceived(OfflinePlayer player) {
        if (player.hasPlayedBefore() || player.isOnline()) {
            UUID uuid = player.getUniqueId();
            if (fsReceived.containsKey(uuid)) {
                return fsReceived.get(uuid);
            }
            return plugin.getConfig().getInt("stats.fsReceived." + uuid);
        }
        return 0;
    }

}
