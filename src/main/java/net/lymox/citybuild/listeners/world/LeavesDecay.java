package net.lymox.citybuild.listeners.world;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeavesDecay implements Listener {

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        Location spawn = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Spawn");
        if(spawn!=null){
            if(event.getBlock().getLocation().distance(spawn)<=100){
                event.setCancelled(true);
            }
        }
        Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");
        if(farmwelt!=null){
            if(isInSpawnRadius(event.getBlock().getLocation(), farmwelt)){
                event.setCancelled(true);
            }
        }
    }

    private boolean isInSpawnRadius(Location eventLocation, Location location) {
        if (location != null) {

            double distanceX = Math.abs(eventLocation.getX() - location.getX());
            double distanceZ = Math.abs(eventLocation.getZ() - location.getZ());

            return distanceX <= 100 && distanceZ <= 100;
        }
        return false;
    }


}
