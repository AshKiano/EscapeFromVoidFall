package com.ashkiano.escapefromvoidfall;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
    private final EscapeFromVoidFall plugin;

    public PlayerListener(EscapeFromVoidFall plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerFallIntoVoid(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() <= -66) {
            plugin.teleportToSafety(player);
        }
    }

    public static Location findSafeLocation(Player player, int searchRadius) {
        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        for (int x = -searchRadius; x <= searchRadius; x++) {
            for (int z = -searchRadius; z <= searchRadius; z++) {
                Location testLocation = playerLocation.clone().add(x, 0, z);
                if (isLocationSafe(testLocation)) {
                    return testLocation;
                }
            }
        }
        return null;
    }

    private static boolean isLocationSafe(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        return world.getBlockAt(x, y - 1, z).getType().isSolid() && world.getBlockAt(x, y, z).getType() == Material.AIR && world.getBlockAt(x, y + 1, z).getType() == Material.AIR;
    }
}