package net.lymox.citybuild.listeners.elytra;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ElytraListener extends BukkitRunnable implements Listener {

    private final CitybuildPlugin plugin;
    private final int multiplyValue;
    private final boolean boostEnabled;
    private final World world;
    private final World worldNether;
    private final Location spawn;
    private final Location spawnNether;
    private final List<Player> flying = new ArrayList<>();
    private final List<Player> boosted = new ArrayList<>();
    private final String message;

    public ElytraListener() {
        this.plugin = CitybuildPlugin.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.multiplyValue = 5;
        this.boostEnabled = true;
        this.message = "Drücke %key% um dich zu boosten";

        this.spawn = plugin.getManagers().getLocationsManager().get("Warp.Farmwelt");
        this.spawnNether = plugin.getManagers().getLocationsManager().get("Warp.Nether");

        this.world = (spawn != null && spawn.getWorld() != null)
                ? spawn.getWorld() : Bukkit.getWorld("Farmwelt");
        this.worldNether = (spawnNether != null && spawnNether.getWorld() != null)
                ? spawnNether.getWorld() : Bukkit.getWorld("NetherFarmwelt");

        this.runTaskTimer(plugin, 0, 3);
    }

    @Override
    public void run() {
        world.getPlayers().forEach(this::handlePlayer);
        worldNether.getPlayers().forEach(this::handlePlayer);
    }

    private void handlePlayer(Player player) {
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) return;

        Location spawnLoc = player.getWorld().equals(worldNether) ? spawnNether : spawn;
        double height = spawnLoc.getBlockY();
        double pHeight = player.getLocation().getBlockY();

        if(player.getWorld().getEnvironment().equals(World.Environment.NETHER)){
            boolean inRadius = isInRadius(player, spawnLoc);
            if(!player.isGliding()) {
                player.setAllowFlight(inRadius);
            }
        }else {
            if (height - pHeight >= 2) {
                boolean inRadius = isInRadius(player, spawnLoc);
                if(!player.isGliding()) {
                    player.setAllowFlight(inRadius);
                }
            }
        }


        // Landung erkennen
        if (flying.contains(player) && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            player.setAllowFlight(false);
            player.setGliding(false);
            boosted.remove(player);
            Bukkit.getScheduler().runTaskLater(plugin, () -> flying.remove(player), 5);
        }

        // Wand-Kollision erkennen und sanft schubsen
        if (flying.contains(player)) {
            Location front = player.getLocation().clone().add(player.getLocation().getDirection().normalize().multiply(0.5));
            if (!front.getBlock().getType().isAir()) {
                player.setVelocity(player.getLocation().getDirection().normalize().multiply(0.6));
            }
        }
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) return;

        Location spawnLoc = player.getWorld().equals(worldNether) ? spawnNether : spawn;
        if (!isInRadius(player, spawnLoc)) return;

        int height = spawnLoc.getBlockY();
        int playerHeight = player.getLocation().getBlockY();
        int differenz = height - playerHeight;

        if (differenz <= -3) return;

        event.setCancelled(true);
        player.setGliding(true);
        player.setAllowFlight(false);
        flying.add(player);

        if (!boostEnabled) return;

        String[] messageParts = message.split("%key%");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new ComponentBuilder(messageParts[0])
                        .append(new KeybindComponent("key.swapOffhand"))
                        .append(messageParts[1])
                        .create());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER &&
                (event.getCause() == EntityDamageEvent.DamageCause.FALL ||
                        event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) &&
                flying.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent event) {
        if (!boostEnabled || !flying.contains(event.getPlayer()) || boosted.contains(event.getPlayer())) return;

        event.setCancelled(true);
        boosted.add(event.getPlayer());
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(multiplyValue));
    }

    @EventHandler
    public void onToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && flying.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean isInRadius(Player player, Location center) {
        if (center == null || !player.getWorld().equals(center.getWorld())) return false;

        double dx = Math.abs(player.getLocation().getX() - center.getX());
        double dz = Math.abs(player.getLocation().getZ() - center.getZ());
        int radius = plugin.getManagers().getServerManager().getSpawnProtectionRadius();

        return dx <= radius && dz <= radius;
    }
}
