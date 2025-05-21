package net.lymox.citybuild.listeners.entitys;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof Monster))return;
        if(event.getEntity().getKiller()==null)return;
        if(!(event.getEntity().getKiller() instanceof Player player))return;
        Userdata userdata = new Userdata(player.getUniqueId());
        Monsterjäger monsterjäger = (Monsterjäger) userdata.getSkill(SkillType.MONSTERJÄGER);
        if(event.getEntity().getType().equals(EntityType.ZOMBIE)){
            monsterjäger.addExp(5);
            monsterjäger.setKills(monsterjäger.getKills()+1);
            player.sendActionBar("§5MONSTERJÄGER §8- §a+5 XP §7[" + monsterjäger.getExp()+"/999999]");
        }
        userdata.saveSkill(monsterjäger);
    }


}
