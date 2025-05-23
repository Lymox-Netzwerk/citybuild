package net.lymox.citybuild.listeners;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.location.World;
import com.plotsquared.core.plot.Plot;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.uuidfetcher.UUIDFetcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final Map<UUID, BossBar> spawnProtectionBars = new HashMap<>();
    private final Map<UUID, BossBar> netherSpawnProtectionBars = new HashMap<>();
    private final Map<UUID, BossBar> plotBars = new HashMap<>();
    private final Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");
    private final Location nether = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Nether");
    private final Location spawn = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Spawn");

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (farmwelt == null || !player.getWorld().equals(farmwelt.getWorld())) {
            removeBossBarSpawnProtection(player);
        }else {
            double dx = player.getLocation().getX() - farmwelt.getX();
            double dz = player.getLocation().getZ() - farmwelt.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance <= CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius()) {
                float progress = (float) Math.max(0.0, Math.min(1.0, 1.0 - (distance / CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius())));
                BossBar bar = spawnProtectionBars.computeIfAbsent(player.getUniqueId(), uuid -> {
                    BossBar newBar = BossBar.bossBar(
                            MiniMessage.miniMessage().deserialize("<white><b>ɪɴ ᴅᴇʀ sᴘᴀᴡɴᴘʀᴏᴛᴇᴄᴛɪᴏɴ"),
                            progress,
                            BossBar.Color.GREEN,
                            BossBar.Overlay.NOTCHED_6
                    );
                    player.showBossBar(newBar);
                    return newBar;
                });

                bar.progress(progress);
                player.showBossBar(bar); // Sicherheitshalber erneut anzeigen
            } else {
                removeBossBarSpawnProtection(player);
            }
        }

        if (nether == null || !player.getWorld().equals(nether.getWorld())) {
            removeBossBarNetherSpawnProtection(player);
        }else {
            double dx = player.getLocation().getX() - nether.getX();
            double dz = player.getLocation().getZ() - nether.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance <= CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius()) {
                float progress = (float) Math.max(0.0, Math.min(1.0, 1.0 - (distance / CitybuildPlugin.getInstance().getManagers().getServerManager().getSpawnProtectionRadius())));
                BossBar bar = netherSpawnProtectionBars.computeIfAbsent(player.getUniqueId(), uuid -> {
                    BossBar newBar = BossBar.bossBar(
                            MiniMessage.miniMessage().deserialize("<white><b>ɪɴ ᴅᴇʀ sᴘᴀᴡɴᴘʀᴏᴛᴇᴄᴛɪᴏɴ"),
                            progress,
                            BossBar.Color.RED,
                            BossBar.Overlay.NOTCHED_6
                    );
                    player.showBossBar(newBar);
                    return newBar;
                });

                bar.progress(progress);
                player.showBossBar(bar); // Sicherheitshalber erneut anzeigen
            } else {
                removeBossBarNetherSpawnProtection(player);
            }
        }

        if(spawn == null || !player.getWorld().equals(spawn.getWorld())){
            removeBossBarPlot(player);
        }else {
            com.plotsquared.core.location.Location location = com.plotsquared.core.location.Location.at(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
            Plot plot = location.getPlot();

            if (plot != null) {

                if(spawn.distance(player.getLocation())<=80){
                    removeBossBarPlot(player);
                }else {
                    if(!plotBars.containsKey(player.getUniqueId())){
                        BossBar newBar = BossBar.bossBar(
                                MiniMessage.miniMessage().deserialize("<white><b>ᴘʟᴏᴛ ᴠᴏɴ " + UUIDFetcher.getName(plot.getOwner())),
                                1,
                                BossBar.Color.WHITE,
                                BossBar.Overlay.NOTCHED_6);
                        plotBars.put(player.getUniqueId(), newBar);
                        player.showBossBar(newBar);
                    }else {
                        BossBar bossBar = plotBars.get(player.getUniqueId());
                        String name = PlainTextComponentSerializer.plainText().serialize(bossBar.name());
                        if(!name.equalsIgnoreCase("ᴘʟᴏᴛ ᴠᴏɴ " + UUIDFetcher.getName(plot.getOwner()))){
                            bossBar.name(MiniMessage.miniMessage().deserialize("<white><b>ᴘʟᴏᴛ ᴠᴏɴ " + UUIDFetcher.getName(plot.getOwner())));
                        }
                    }
                }


            } else {
                removeBossBarPlot(player);
            }
        }
    }

    private void removeBossBarSpawnProtection(Player player) {
        BossBar bar = spawnProtectionBars.remove(player.getUniqueId());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }

    private void removeBossBarNetherSpawnProtection(Player player) {
        BossBar bar = netherSpawnProtectionBars.remove(player.getUniqueId());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }

    private void removeBossBarPlot(Player player) {
        BossBar bar = plotBars.remove(player.getUniqueId());
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }



}
