package com.ashkiano.escapefromvoidfall;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EscapeFromVoidFall extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("efvf.reload")) {
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "[EFVF] Configuration reloaded.");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command.");
            }
            return true;
        } else if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("HelpMessages.banner.msg")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("HelpMessages.help.msg")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("HelpMessages.reload.msg")));
            return true;
        }
        return false;
    }

    public void teleportToSafety(Player player) {
        Location safeLocation = PlayerListener.findSafeLocation(player, getConfig().getInt("SearchRadius"));
        if (safeLocation == null) {
            safeLocation = player.getWorld().getSpawnLocation();
        }

        // Reset fall distance before teleporting
        player.setFallDistance(0);

        player.teleport(safeLocation);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Teleporting").replace("%player%", player.getName())));

        // Apply any configured punishment
        if (getConfig().getBoolean("Punish.enabled")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(getConfig().getString("Punish.type")), getConfig().getInt("Punish.duration"), getConfig().getInt("Punish.level")));
        }

        // Ensure the player's fall damage is reset after teleportation
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> player.setFallDistance(0), 1L);
    }
}