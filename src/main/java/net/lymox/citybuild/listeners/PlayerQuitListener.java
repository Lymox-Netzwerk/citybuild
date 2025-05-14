package net.lymox.citybuild.listeners;

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
        event.setQuitMessage("§c« " + lymoxPlayer.displayName() + "§7" + player.getName() + " hat CityBuild verlassen");
    }


}
