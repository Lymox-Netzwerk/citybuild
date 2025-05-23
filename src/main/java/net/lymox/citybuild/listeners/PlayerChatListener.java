package net.lymox.citybuild.listeners;

import net.luckperms.api.LuckPermsProvider;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.core.master.manager.PermissionManager;
import net.lymox.core.master.objects.player.LymoxPlayer;
import net.lymox.core.master.service.LymoxPlayerService;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void handleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        LymoxPlayer lymoxPlayer = CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(player.getUniqueId());
        String message = event.getMessage().replaceAll("%", "%%");
        PermissionManager permissionManager = new PermissionManager(LuckPermsProvider.get());
        if(permissionManager.getPlayerGroup(player.getUniqueId()).getName().equalsIgnoreCase("admin")) {
            event.setFormat("§8〢 " + lymoxPlayer.displayName() + " §7" + player.getName() + " §8» §6" + message.replaceAll("&","§"));

        } else {
            event.setFormat("§8〢 " + lymoxPlayer.displayName() + "§7" + player.getName() + " §8» §7" + message);
        }
    }

}
