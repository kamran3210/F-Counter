package com.kamran.fcounter;

import com.kamran.fcounter.commands.FCounterCommands;
import com.kamran.fcounter.commands.FCounterTabCompleter;
import com.kamran.fcounter.events.FCounterEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class FCounter extends JavaPlugin {

    private static FCounter plugin;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();
        StatManager.loadStats();
        getCommand("fcounter").setExecutor(new FCounterCommands());
        getCommand("fcounter").setTabCompleter(new FCounterTabCompleter());
        getServer().getPluginManager().registerEvents(new FCounterEvents(), plugin);
    }

    @Override
    public void onDisable() {
        StatManager.saveStats();
    }

    public static FCounter getPlugin() {
        return plugin;
    }

}
