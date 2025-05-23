package net.lymox.citybuild.listeners.npc.shop;

import com.sun.jdi.Locatable;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobInteractEvent;
import io.lumine.mythic.bukkit.events.MythicMobPreSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.popcraft.chunky.api.ChunkyAPI;

import java.util.Random;

public class ShopPreventDeathListener implements Listener {

    private final Random random = new Random();
    private final int maxRadius = 5000; // z.B. 5000 Block-Radius

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(MythicBukkit.inst().getMobManager().isMythicMob(event.getEntity())){
            ActiveMob mythicMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(event.getEntity());
            System.out.println(mythicMob.getMobType());
            if(mythicMob.getMobType().equalsIgnoreCase("fantasy_npc_banker")){
                event.setCancelled(true);
            }
            if(mythicMob.getMobType().equalsIgnoreCase("fantasy_npc_knight")){
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onMythicMobInteract(MythicMobInteractEvent event) {
        if(event.getActiveMob().getMobType().equalsIgnoreCase("fantasy_npc_banker")){
            Player player = event.getPlayer();
            Categorie categorie = CitybuildPlugin.getInstance().getManagers().getShopManager().getCategories().getFirst();
            if(categorie==null){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Shop ist noch nicht eingerichtet! Bitte wende dich an ein Teammitglied.")));
                return;
            }
            Inventory shop = new GUIManager().openShop(categorie, false);
            player.openInventory(shop);
        }
        if(event.getActiveMob().getMobType().equalsIgnoreCase("fantasy_npc_knight")){
            Player player = event.getPlayer();
            Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");

            if(farmwelt != null){
                if(player.getWorld().equals(farmwelt.getWorld())){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Location wird gesucht...")));
                    Location randomLocation = getRandomSurfaceLocation(farmwelt.getWorld(), maxRadius);
                    if(randomLocation==null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Es konnte keine passene Position gefunden werden, bitte versuche es erneut!")));
                        return;
                    }
                    player.teleport(randomLocation);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                }
            }
        }
    }

    private Location getRandomSurfaceLocation(World world, int radius) {
        for (int i = 0; i < 20; i++) { // Versuche bis zu 20 zufällige Positionen
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;

            int highestY = world.getHighestBlockYAt(x, z);
            Block block = world.getBlockAt(x, highestY - 1, z);

            // Ignoriere gefährliche oder unbrauchbare Blöcke
            if (block.isPassable() || block.getType().name().contains("WATER") || block.getType().name().contains("LAVA")) {
                continue;
            }

            return new Location(world, x + 0.5, highestY, z + 0.5); // In der Mitte des Blocks
        }

        return null; // Kein guter Ort gefunden
    }


}
