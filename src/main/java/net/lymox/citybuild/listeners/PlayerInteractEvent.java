package net.lymox.citybuild.listeners;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEvent implements Listener {

    private final Location spawn = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Spawn");

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(spawn.distance(player.getLocation())>80){
            if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
            event.setCancelled(true);
        }
    }


}
