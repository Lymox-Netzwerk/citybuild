package net.lymox.citybuild.listeners.world;

import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import net.kyori.adventure.text.Component;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Holzfäller;
import net.lymox.citybuild.utils.userdata.skills.Miner;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.*;

public class WorldBreakListener implements Listener {
    Location farmwelt = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Farmwelt");
    Location spawn = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Spawn");
    Location nether = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.Nether");
    Location end = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.End");
    private List<Block> placed = new ArrayList<>();
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();


        if(farmwelt != null){
            if(isInSpawnRadius(player.getLocation(), farmwelt)){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                event.setCancelled(true);
            }else {
                if(player.getWorld().equals(farmwelt.getWorld())){
                    Block block = event.getBlock();
                    Userdata userdata = new Userdata(player.getUniqueId());
                    Miner miner = (Miner) userdata.getSkill(SkillType.MINER);
                    if(miner.getLevel()<10) {
                        if (miner.isValidBlock(block)) {
                            int drop = miner.dropsExp(block);
                            int oldLevel = miner.getLevel();
                            miner.addExp(drop);
                            int newLevel = miner.getLevel();
                            miner.setBlocks(miner.getBlocks() + 1);
                            player.sendActionBar("\uE07E §7"+miner.getNameFormatted()+" §8- §a+" + drop + " XP §7[" + miner.getExp() + "/" + (miner.requiredExp(oldLevel) >= miner.getExp() ? "" + miner.requiredExp(oldLevel) : "" + miner.requiredExp(oldLevel + 1)) + "]");
                            if (oldLevel != newLevel) {
                                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                                player.sendMessage("§k-------------------------");
                                player.sendMessage("   §a§lLEVEL AUFGESTIEGEN");
                                player.sendMessage("§7 ");
                                player.sendMessage("              \uE07E");
                                player.sendMessage("§7 ");
                                player.sendMessage(miner.getNameFormatted()+"§8 " + oldLevel + " §7› §a"+miner.getNameFormatted()+" " + newLevel);
                                player.sendMessage("§7 ");
                                player.sendMessage("§7ʙᴇʟᴏʜɴᴜɴɢᴇɴ");
                                for (Component reward : miner.rewards(newLevel, false)) {
                                    player.sendMessage(reward);
                                }
                                player.sendMessage("§k-------------------------");
                                userdata.setMünzen(userdata.getMünzen() + miner.münzenReward(newLevel));
                            }
                            userdata.saveSkill(miner);
                        }
                    }
                    Holzfäller holzfäller = (Holzfäller) userdata.getSkill(SkillType.HOLZFÄLLER);
                    if(holzfäller.getLevel()<10) {
                        if (holzfäller.isValidBlock(block)) {
                            int drop = holzfäller.dropsExp(block);
                            int oldLevel = holzfäller.getLevel();
                            holzfäller.addExp(drop);
                            int newLevel = holzfäller.getLevel();
                            holzfäller.setBlocks(holzfäller.getBlocks() + 1);
                            player.sendActionBar("\uE07F §7"+holzfäller.getNameFormatted()+" §8- §a+" + drop + " XP §7[" + holzfäller.getExp() + "/" + (holzfäller.requiredExp(oldLevel) >= holzfäller.getExp() ? "" + holzfäller.requiredExp(oldLevel) : "" + holzfäller.requiredExp(oldLevel + 1)) + "]");
                            if (oldLevel != newLevel) {
                                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                                player.sendMessage("§k-------------------------");
                                player.sendMessage("   §a§lLEVEL AUFGESTIEGEN");
                                player.sendMessage("§7 ");
                                player.sendMessage("              \uE07F");
                                player.sendMessage("§7 ");
                                player.sendMessage(holzfäller.getNameFormatted()+"§8 " + oldLevel + " §7› §a"+holzfäller.getNameFormatted()+" " + newLevel);
                                player.sendMessage("§7 ");
                                player.sendMessage("§7ʙᴇʟᴏʜɴᴜɴɢᴇɴ");
                                for (Component reward : holzfäller.rewards(newLevel, false)) {
                                    player.sendMessage(reward);
                                }
                                player.sendMessage("§k-------------------------");
                                userdata.setMünzen(userdata.getMünzen() + miner.münzenReward(newLevel));
                            }
                            userdata.saveSkill(holzfäller);

                        }
                    }else {
                        if (!isLog(block)) return;

                        Set<Block> visited = new HashSet<>();
                        Queue<Block> toCheck = new LinkedList<>();
                        toCheck.add(block);

                        while (!toCheck.isEmpty()) {
                            Block current = toCheck.poll();
                            if (visited.contains(current)) continue;
                            visited.add(current);

                            if (!isLog(current)) continue;

                            // Baue den Block ab
                            current.breakNaturally();

                            // Füge angrenzende Blöcke zur Queue hinzu (3x3x3 Umgebung)
                            for (int dx = -1; dx <= 1; dx++) {
                                for (int dy = -1; dy <= 1; dy++) {
                                    for (int dz = -1; dz <= 1; dz++) {
                                        Block neighbor = current.getRelative(dx, dy, dz);
                                        if (!visited.contains(neighbor)) {
                                            toCheck.add(neighbor);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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

        if(end != null){
            if(end.getWorld().equals(player.getWorld())){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                if(event.getBlock().getType().equals(Material.BEACON)&&!placed.contains(event.getBlock())){
                    event.setCancelled(true);
                }
                if(end.distance(player.getLocation())<=10) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isLog(Block block) {
        Material type = block.getType();
        return type.name().endsWith("_LOG") || type.name().endsWith("_STEM");
    }

    @EventHandler
    public void onFurnitureBreak(FurnitureBreakEvent event) {
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
        if(end != null){
            if(end.getWorld().equals(player.getWorld())){
                if(player.hasPermission("lymox.citybuild.farmwelt.spawnprotection.bypass")&&player.getGameMode().equals(GameMode.CREATIVE))return;
                if(end.distance(player.getLocation())<=10) {
                    event.setCancelled(true);
                    return;
                }
                if(event.getBlock().getType().equals(Material.BEACON)){
                    placed.add(event.getBlock());
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
