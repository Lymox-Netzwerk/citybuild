package net.lymox.citybuild.listeners.entitys;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.model.user.UserManager;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Holzfäller;
import net.lymox.citybuild.utils.userdata.skills.Jäger;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import net.lymox.citybuild.uuidfetcher.UUIDFetcher;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EntityDeathListener implements Listener {



    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().getKiller()==null)return;
        if(!(event.getEntity().getKiller() instanceof Player player))return;
        Userdata userdata = new Userdata(player.getUniqueId());
        Monsterjäger monsterjäger = (Monsterjäger) userdata.getSkill(SkillType.MONSTERJÄGER);
        if(monsterjäger.getLevel()<10) {
            if (monsterjäger.isValidMonster(event.getEntity().getType())) {
                int drop = monsterjäger.dropsExp(event.getEntity());
                int oldLevel = monsterjäger.getLevel();
                monsterjäger.addExp(drop);
                int newLevel = monsterjäger.getLevel();
                monsterjäger.setKills(monsterjäger.getKills() + 1);
                player.sendActionBar("\uE07B §7ᴍᴏɴsᴛᴇʀᴊäɢᴇʀ §8- §a+" + drop + " XP §7[" + monsterjäger.getExp() + "/" + (monsterjäger.requiredExp(oldLevel) >= monsterjäger.getExp() ? "" + monsterjäger.requiredExp(oldLevel) : "" + monsterjäger.requiredExp(oldLevel + 1)) + "]");
                if (oldLevel != newLevel) {
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                    player.sendMessage("§k-------------------------");
                    player.sendMessage("   §a§lLEVEL AUFGESTIEGEN");
                    player.sendMessage("§7 ");
                    player.sendMessage("              \uE07B");
                    player.sendMessage("§7 ");
                    player.sendMessage("§8ᴍᴏɴsᴛᴇʀᴊäɢᴇʀ " + oldLevel + " §7› §aᴍᴏɴsᴛᴇʀᴊäɢᴇʀ " + newLevel);
                    player.sendMessage("§7 ");
                    player.sendMessage("§7ʙᴇʟᴏʜɴᴜɴɢᴇɴ");
                    for (Component reward : monsterjäger.rewards(newLevel, false)) {
                        player.sendMessage(reward);
                    }
                    player.sendMessage("§k-------------------------");
                    userdata.setMünzen(userdata.getMünzen() + monsterjäger.münzenReward(newLevel));
                }
            }
        }
        Jäger jäger = (Jäger) userdata.getSkill(SkillType.JÄGER);
        if(jäger.getLevel()<10) {
            if (jäger.isValidMonster(event.getEntity().getType())) {
                int drop = jäger.dropsExp(event.getEntity());
                int oldLevel = jäger.getLevel();
                jäger.addExp(drop);
                int newLevel = jäger.getLevel();
                jäger.setKills(jäger.getKills() + 1);
                player.sendActionBar("\uE07D §7"+jäger.getNameFormatted()+" §8- §a+" + drop + " XP §7[" + jäger.getExp() + "/" + (jäger.requiredExp(oldLevel) >= jäger.getExp() ? "" + jäger.requiredExp(oldLevel) : "" + jäger.requiredExp(oldLevel + 1)) + "]");
                if (oldLevel != newLevel) {
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                    player.sendMessage("§k-------------------------");
                    player.sendMessage("   §a§lLEVEL AUFGESTIEGEN");
                    player.sendMessage("§7 ");
                    player.sendMessage("              \uE07D");
                    player.sendMessage("§7 ");
                    player.sendMessage("§8"+jäger.getNameFormatted()+" " + oldLevel + " §7› §a"+jäger.getNameFormatted()+" " + newLevel);
                    player.sendMessage("§7 ");
                    player.sendMessage("§7ʙᴇʟᴏʜɴᴜɴɢᴇɴ");
                    for (Component reward : jäger.rewards(newLevel, false)) {
                        player.sendMessage(reward);
                    }
                    player.sendMessage("§k-------------------------");
                    userdata.setMünzen(userdata.getMünzen() + jäger.münzenReward(newLevel));
                }
            }
        }
        userdata.saveSkill(jäger);

        Holzfäller holzfäller = (Holzfäller) userdata.getSkill(SkillType.HOLZFÄLLER);
        int dropchance = holzfäller.doubleDropChance(holzfäller.getLevel());
        int random = new Random().nextInt(0, 100);
        if(dropchance>=random){
            List<ItemStack> originalDrops = new ArrayList<>(event.getDrops());

            for (ItemStack drop : originalDrops) {
                event.getDrops().add(drop.clone());
            }
        }
    }

    @EventHandler
    public void onDragonDeathEvent(EntityDeathEvent event) {
        if(event.getEntity().getType().equals(EntityType.ENDER_DRAGON)){
            HashMap<UUID, Double> players = EntityDamageListener.players;
            final String[] allPlayers = {""};

            players.forEach((uuid, damage) -> {
                if(allPlayers[0].isEmpty()){
                    allPlayers[0] = UUIDFetcher.getName(uuid);
                }else {
                    allPlayers[0] = allPlayers[0] + ", " + UUIDFetcher.getName(uuid);
                }
                if(damage >= 50){
                    Player onlinePlayer = Bukkit.getPlayer(uuid);
                    if(onlinePlayer!=null) {
                        Userdata userdata = new Userdata(uuid);
                        userdata.setMünzen(userdata.getMünzen() + 1000);
                    }
                }else {
                    Player onlinePlayer = Bukkit.getPlayer(uuid);
                    if(onlinePlayer!=null) {
                        if(onlinePlayer.getWorld().equals(event.getEntity().getWorld())) {
                            Userdata userdata = new Userdata(uuid);
                            userdata.setMünzen(userdata.getMünzen() + 1000);

                            if(event.getEntity().getKiller() != null){
                                if(event.getEntity().getKiller() instanceof Player player) {
                                    if(!onlinePlayer.equals(player)){
                                        onlinePlayer.sendActionBar("§fDu hast §e1000 \ue03c §ferhalten");
                                    }
                                }
                            }else {
                                onlinePlayer.sendActionBar("§fDu hast §e1000 \ue03c §ferhalten");
                            }

                        }
                    }
                }
            });
            Bukkit.broadcastMessage("§k----------------------");
            Bukkit.broadcastMessage("§5§lDER ENDERDRACHE IST GEFALLEN");
            if(event.getEntity().getKiller() != null){
                if(event.getEntity().getKiller() instanceof Player player){
                    Userdata userdata = new Userdata(player.getUniqueId());
                    userdata.setMünzen(userdata.getMünzen() + 1000);
                    player.sendActionBar("§fDu hast §e2000 \ue03c §ferhalten");
                    Bukkit.broadcastMessage("§7Der finale Schlag wurde von §b" + CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(player.getUniqueId()).displayName() + player.getName() + "§7 ausgeführt");
                }
            }
            Bukkit.broadcastMessage("§7ᴠɪᴇʟᴇɴ ᴅᴀɴᴋ ᴀɴ: " + allPlayers[0]);
            Bukkit.broadcastMessage("§k----------------------");
        }
    }


}
