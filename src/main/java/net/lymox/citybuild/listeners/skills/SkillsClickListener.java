package net.lymox.citybuild.listeners.skills;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import net.lymox.citybuild.utils.userdata.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SkillsClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType().equals(Material.AIR))
            return;
        if (event.getView().title() == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if(title.equalsIgnoreCase("sᴋɪʟʟs")){
            event.setCancelled(true);
            if(event.getRawSlot()==0){
                player.openInventory(new GUIManager().openSkills(player.getUniqueId(), SkillType.MONSTERJÄGER));
            }
            if(event.getRawSlot()==1){
                player.openInventory(new GUIManager().openSkills(player.getUniqueId(), SkillType.JÄGER));
            }
            if(event.getRawSlot()==2){
                player.openInventory(new GUIManager().openSkills(player.getUniqueId(), SkillType.MINER));
            }
            if(event.getRawSlot()==3){
                player.openInventory(new GUIManager().openSkills(player.getUniqueId(), SkillType.HOLZFÄLLER));
            }
        }
        if(title.equalsIgnoreCase("sᴋɪʟʟs ● ᴍᴏɴsᴛᴇʀᴊäɢᴇʀ")){
            event.setCancelled(true);
        }
    }


}
