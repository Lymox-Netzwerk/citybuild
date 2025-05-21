package net.lymox.citybuild.listeners;

import net.lymox.citybuild.manager.ServerManager;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.core.master.objects.player.LymoxPlayer;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LymoxPlayer lymoxPlayer = CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(player.getUniqueId());
        ServerManager serverManager = CitybuildPlugin.getInstance().getManagers().getServerManager();
        if(serverManager.isMaintenance()){
           if(!player.hasPermission(serverManager.getMaintenancePermission())){
               event.quitMessage(null);
               return;
           }
        }
        event.setQuitMessage("§c« " + lymoxPlayer.displayName() + "§7" + player.getName() + " hat CityBuild verlassen");
    }


}
