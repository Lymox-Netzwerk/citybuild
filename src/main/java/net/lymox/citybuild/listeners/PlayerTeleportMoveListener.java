package net.lymox.citybuild.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.commands.WarpCommand;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeleportMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        List<Integer> inte = new ArrayList<>();
        WarpCommand.teleportReq.forEach((integer, target) -> {
            if(target.equals(player)){
                if (event.getFrom().getX() != event.getTo().getX()
                        || event.getFrom().getY() != event.getTo().getY()
                        || event.getFrom().getZ() != event.getTo().getZ()) {
                    inte.add(integer);
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Die Teleportation wurde Aufgrund einer Bewegung unterbrochen")));
                }
            }
        });

        for (Integer integer : inte) {
            WarpCommand.teleportReq.remove(integer);
        }
        inte.clear();
    }


}
