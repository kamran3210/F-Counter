package com.kamran.fcounter.commands;

import com.kamran.fcounter.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FCounterCommands implements CommandExecutor {

    private void stats(CommandSender sender, OfflinePlayer player) {
        int fsGiven = StatManager.getFsGiven(player);
        int fsReceived = StatManager.getFsReceived(player);
        sender.sendMessage(ChatColor.AQUA + player.getName() + "'s stats:");
        sender.sendMessage(ChatColor.AQUA + "F's received: " + ChatColor.WHITE + fsReceived);
        sender.sendMessage(ChatColor.AQUA + "F's given: " + ChatColor.WHITE + fsGiven);
    }

    private void reset(CommandSender sender, OfflinePlayer player) {
        StatManager.resetFsGiven(player);
        StatManager.resetFsReceived(player);
        sender.sendMessage(ChatColor.AQUA + player.getName() + "'s stats have been reset!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {

            // /fCounter stats
            if (args[0].equalsIgnoreCase("stats")) { // Get sender's info
                if (sender instanceof Player) {
                    stats(sender, (Player) sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player to use this command without targeting another player!");
                }
                return true;
            }

        }
        if (args.length == 2) {

            // /fCounter stats <player>
            if (args[0].equalsIgnoreCase("stats")) { // Get target player's info
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if (player.isOnline() || player.hasPlayedBefore()) {
                    stats(sender, player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found!");
                }
                return true;
            }

            // /fCounter reset <player>
            if (args[0].equalsIgnoreCase("reset")) {
                if (sender.isOp()) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (player.isOnline() || player.hasPlayedBefore()) {
                        reset(sender, player);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be an operator to use that command!");
                }
                return true;
            }

        }
        return false;
    }

}
