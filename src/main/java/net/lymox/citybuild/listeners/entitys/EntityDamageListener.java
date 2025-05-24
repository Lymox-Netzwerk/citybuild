package net.lymox.citybuild.listeners.entitys;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    public static HashMap<UUID, Double> players = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!event.getEntity().getType().equals(EntityType.ENDER_DRAGON))return;
        if(!(event.getDamager() instanceof Player player))return;
        Location end = CitybuildPlugin.getInstance().getManagers().getLocationsManager().get("Warp.End");
        if(end != null){
            if(event.getEntity().getLocation().getWorld().equals(end.getWorld())){
                if(!players.containsKey(player.getUniqueId())){
                    players.put(player.getUniqueId(), event.getDamage());
                }else {
                    double dmg = players.get(player.getUniqueId());
                    players.replace(player.getUniqueId(), (dmg+event.getDamage()));
                }
            }
        }
    }

    @EventHandler
    public void onDamageSkillListener(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Monster){
            if(event.getDamager() instanceof Player player){
                Userdata userdata = new Userdata(player.getUniqueId());
                Monsterjäger monsterjäger = (Monsterjäger) userdata.getSkill(SkillType.MONSTERJÄGER);
                int additionalDamage = monsterjäger.additionalDamage(monsterjäger.getLevel());

                double baseDamage = event.getDamage();

                double bonusDamage = baseDamage * (additionalDamage / 100.0);

                event.setDamage(baseDamage + bonusDamage);
            }
        }
    }


}
