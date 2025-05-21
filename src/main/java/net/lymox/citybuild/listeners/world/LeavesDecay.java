package net.lymox.citybuild.listeners.world;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Location;
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
    }


}
