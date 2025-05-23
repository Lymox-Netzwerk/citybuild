package net.lymox.citybuild.listeners;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player player)){
            return;
        }
        Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");

        if(farmwelt != null){
            if(isInSpawnRadius(player.getLocation(), farmwelt)){
                event.setCancelled(true);
            }
        }
        Location nether = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Nether");

        if(nether != null){
            if(isInSpawnRadius(player.getLocation(), nether)){
                event.setCancelled(true);
            }
        }
    }

    private boolean isInSpawnRadius(Location eventLocation, Location location) {
        if (location != null) {

            double distanceX = Math.abs(eventLocation.getX() - location.getX());
            double distanceZ = Math.abs(eventLocation.getZ() - location.getZ());

            return distanceX <= CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius() && distanceZ <= CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius();
        }
        return false;
    }


}
