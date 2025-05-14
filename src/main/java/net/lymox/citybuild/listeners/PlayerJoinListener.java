package net.lymox.citybuild.listeners;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.core.master.objects.player.LymoxPlayer;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LymoxPlayer lymoxPlayer = CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(player.getUniqueId());
        event.setJoinMessage("§a» " + lymoxPlayer.displayName() + "§7" + player.getName() + " hat CityBuild betreten");

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.hasPlayedBefore()){
                    Location spawn = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Spawn");
                    if(spawn!=null){
                        player.teleport(spawn);
                    }

                    //Starter-Set
                    player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                    player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                    player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                    player.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));
                    player.getInventory().addItem(new ItemStack(Material.OAK_LOG, 16));
                    player.getInventory().addItem(new ItemStack(Material.BREAD, 32));
                }
            }
        }.runTaskLater(CitybuildPlugin.getInstance(), 5);
    }


}
