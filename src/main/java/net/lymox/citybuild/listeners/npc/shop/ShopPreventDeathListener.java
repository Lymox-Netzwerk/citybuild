package net.lymox.citybuild.listeners.npc.shop;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobPreSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class ShopPreventDeathListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(MythicBukkit.inst().getMobManager().isMythicMob(event.getEntity())){
            ActiveMob mythicMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(event.getEntity());
            System.out.println(mythicMob.getMobType());
            if(mythicMob.getMobType().equalsIgnoreCase("fantasy_npc_banker")){
                event.setCancelled(true);
            }
        }
    }


}
