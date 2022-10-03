package com.kamran.fcounter.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FCounterTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("Stats");
            if (sender.isOp()) {
                completions.add("Reset");
            }
            return completions;
        }

        if (args.length == 2) {
            List<String> completions = new ArrayList<>();
            Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
            Bukkit.getOnlinePlayers().toArray(players);
            for (Player p : players) {
                completions.add(p.getName());
            }
            return completions;
        }

        return null;
    }

}
