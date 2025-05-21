package net.lymox.citybuild.listeners.crates;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CratesClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType().equals(Material.AIR))
            return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if(title.contains("ɪɴʜᴀʟᴛ ᴠᴏᴍ") && title.contains("ᴄʀᴀᴛᴇ") && !title.contains("ʙᴇᴀʀʙᴇɪᴛᴇɴ")) {
            event.setCancelled(true);
        }
    }

}
