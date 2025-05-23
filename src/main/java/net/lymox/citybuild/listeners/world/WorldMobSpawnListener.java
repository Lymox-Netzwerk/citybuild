package net.lymox.citybuild.listeners.world;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldMobSpawnListener implements Listener {
    Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");
    Location nether = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Nether");

    public WorldMobSpawnListener() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CitybuildPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(farmwelt != null){
                    for (Entity entity : farmwelt.getWorld().getEntities()) {
                        if(entity instanceof Monster){
                            if(isInSpawnRadius(entity.getLocation(), farmwelt)){
                                CreatureSpawnEvent.SpawnReason reason = entity.getEntitySpawnReason();
                                if(reason.equals(CreatureSpawnEvent.SpawnReason.NATURAL)) entity.remove();
                            }
                        }
                    }
                }


                if(nether != null){
                    for (Entity entity : nether.getWorld().getEntities()) {
                        if(entity instanceof Monster){
                            if(isInSpawnRadius(entity.getLocation(), nether)){
                                CreatureSpawnEvent.SpawnReason reason = entity.getEntitySpawnReason();
                                    if(reason.equals(CreatureSpawnEvent.SpawnReason.NATURAL)) entity.remove();
                            }
                        }else if(entity.getType().equals(EntityType.GHAST) || entity.getType().equals(EntityType.STRIDER) || entity.getType().equals(EntityType.MAGMA_CUBE) || entity.getType().equals(EntityType.HOGLIN)){
                            if (isInSpawnRadius(entity.getLocation(), nether)) {
                                if (entity.getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                                    entity.remove();
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 20);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if(event.getEntity() instanceof Monster){


            if(farmwelt != null){
                if(event.getLocation().getWorld().equals(farmwelt.getWorld())) {

                    if (isInSpawnRadius(event.getLocation(), farmwelt)) {
                        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                            event.setCancelled(true);
                        }
                    }

                }
            }


            if(nether != null){
                if(event.getLocation().getWorld().equals(nether.getWorld())) {
                    if (isInSpawnRadius(event.getLocation(), nether)) {
                        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

        if(event.getEntity().getType().equals(EntityType.GHAST) || event.getEntity().getType().equals(EntityType.STRIDER) || event.getEntity().getType().equals(EntityType.MAGMA_CUBE) || event.getEntity().getType().equals(EntityType.HOGLIN)){
            if(nether != null){
                if(event.getLocation().getWorld().equals(nether.getWorld())) {
                    if (isInSpawnRadius(event.getLocation(), nether)) {
                        if (event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

    private boolean isInSpawnRadius(Location eventLocation, Location location) {
        if (location != null) {
            if (!eventLocation.getWorld().equals(location.getWorld())) return false;

            double distanceX = Math.abs(eventLocation.getX() - location.getX());
            double distanceZ = Math.abs(eventLocation.getZ() - location.getZ());

            return distanceX <= CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius() && distanceZ <= CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius();
        }
        return false;
    }


}
