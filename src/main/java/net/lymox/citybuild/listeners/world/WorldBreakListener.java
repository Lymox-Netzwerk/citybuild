package net.lymox.citybuild.listeners.world;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class WorldBreakListener implements Listener {
    Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");
    Location spawn = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Spawn");
    Location nether = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Nether");
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();


        if(farmwelt != null){
            if(isInSpawnRadius(player.getLocation(), farmwelt)){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                event.setCancelled(true);
            }
        }

        if(nether != null){
            if(isInSpawnRadius(player.getLocation(), nether)){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                event.setCancelled(true);
            }
        }

        if(spawn != null){
            if(spawn.getWorld().equals(player.getWorld())){
                if(spawn.distance(player.getLocation())<=85){
                    if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(farmwelt != null){
            if(isInSpawnRadius(player.getLocation(), farmwelt)){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                System.out.println("Cancled place");
                event.setCancelled(true);
            }
        }
        if(nether != null){
            if(isInSpawnRadius(player.getLocation(), nether)){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                event.setCancelled(true);
            }
        }
        if(spawn != null){
            if(spawn.getWorld().equals(player.getWorld())){
                if(spawn.distance(player.getLocation())<=85){
                    if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(farmwelt != null){
            if(isInSpawnRadius(event.getBlock().getLocation(), farmwelt)){
                event.setCancelled(true);
            }
        }
        if(nether != null){
            if(isInSpawnRadius(event.getBlock().getLocation(), nether)){
                event.setCancelled(true);
            }
        }
        if(spawn != null){
            if(spawn.getWorld().equals(event.getBlock().getLocation().getWorld())){
                if(spawn.distance(event.getBlock().getLocation())<=85){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        if(farmwelt != null){
            if(isInSpawnRadius(event.getLocation(), farmwelt)){
                event.setCancelled(true);
            }
        }

        if(nether != null){
            if(isInSpawnRadius(event.getLocation(), nether)){
                event.setCancelled(true);
            }
        }

        if(spawn != null){
            if(spawn.getWorld().equals(event.getLocation().getWorld())){
                if(spawn.distance(event.getLocation())<=85){
                    event.setCancelled(true);
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
