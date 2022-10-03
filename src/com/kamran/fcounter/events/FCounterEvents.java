package com.kamran.fcounter.events;

import com.kamran.fcounter.FCounter;
import com.kamran.fcounter.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class FCounterEvents implements Listener {

    private final FCounter plugin = FCounter.getPlugin();
    private final int fTimeOut = plugin.getConfig().getInt("fTimeOut");
    private Player lastDeath;
    private ArrayList<UUID> fsGivenBy = new ArrayList<>();
    private BukkitTask resetLastDeathTask;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        lastDeath = player;
        fsGivenBy = new ArrayList<>();
        // Send a message after the death message
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GRAY + "F's in the chat for " + player.getDisplayName());
            }
        }.runTask(plugin);

        // Reset the last died tracker after specified time so no more F's can be given
        if (resetLastDeathTask != null) {
            resetLastDeathTask.cancel();
        }
        resetLastDeathTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!lastDeath.isOnline()) { // Player who last died is now offline, so unload their stats
                    StatManager.saveIndividualStats(lastDeath);
                }
                Bukkit.broadcastMessage(ChatColor.GRAY + "No more F's for " + player.getDisplayName());
                lastDeath = null;
            }
        }.runTaskLater(plugin, 20 * fTimeOut);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        if (lastDeath != null) {
            String message = event.getMessage();
            Player sender = event.getPlayer();
            if (sender != lastDeath) {
                if (message.replaceAll("[^a-zA-Z0-9]", "").equalsIgnoreCase("F")) {
                    UUID senderUUID = sender.getUniqueId();
                    if (!fsGivenBy.contains(senderUUID)) { // Only give an F if one has not already been given by this player
                        fsGivenBy.add(senderUUID);
                        StatManager.incrementFsGiven(sender);
                        StatManager.incrementFsReceived(lastDeath);
                        // Send a message after the player's message
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sender.sendMessage(ChatColor.GRAY + "F given to " + lastDeath.getDisplayName());
                            }
                        }.runTask(plugin);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StatManager.loadIndividualStats(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player != lastDeath) { // Do not unload stats of the player who last died
            StatManager.saveIndividualStats(player);
        }
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        if (event.getWorld().getEnvironment() == World.Environment.NORMAL) {
            StatManager.saveStats();
        }
    }

}
