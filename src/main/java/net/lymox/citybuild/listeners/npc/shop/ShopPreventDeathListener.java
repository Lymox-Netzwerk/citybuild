package net.lymox.citybuild.listeners.npc.shop;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobInteractEvent;
import io.lumine.mythic.bukkit.events.MythicMobPreSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;

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

    @EventHandler
    public void onMythicMobInteract(MythicMobInteractEvent event) {
        if(event.getActiveMob().getMobType().equalsIgnoreCase("fantasy_npc_banker")){
            Player player = event.getPlayer();
            Categorie categorie = CitybuildPlugin.getInstance().getManagers().getShopManager().getCategories().getFirst();
            if(categorie==null){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Shop ist noch nicht eingerichtet! Bitte wende dich an ein Teammitglied.")));
                return;
            }
            Inventory shop = new GUIManager().openShop(categorie);
            player.openInventory(shop);
        }
    }


}
