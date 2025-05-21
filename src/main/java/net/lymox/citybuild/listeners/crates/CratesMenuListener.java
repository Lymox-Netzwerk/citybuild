package net.lymox.citybuild.listeners.crates;

import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.manager.CratesManager;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.crates.Crate;
import net.lymox.citybuild.manager.objects.crates.CrateOpener;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

public class CratesMenuListener implements Listener {

    private CratesManager cratesManager = CitybuildPlugin.getInstance().getManagers().getCratesManager();

    @EventHandler
    public void onInteract(FurnitureInteractEvent event){
        if(event.getFurniture().getNamespacedID().equalsIgnoreCase("pixelmine_iron_crates:iron_crate_1")){
            event.getPlayer().openInventory(new GUIManager().openCratesMenu(event.getPlayer()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType().equals(Material.AIR) || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().displayName() == null)
            return;
        if (event.getView().title() == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (title.equalsIgnoreCase("ᴄʀᴀᴛᴇs")) {
            event.setCancelled(true);
            if (!event.getCurrentItem().getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
                NamespacedKey namespacedKey = new NamespacedKey(CitybuildPlugin.getInstance(), "crate");
                String idStr = event.getCurrentItem().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
                if(idStr!=null) {
                    int id = Integer.parseInt(idStr);
                    Crate crate = cratesManager.getCrate(id);
                    if (crate != null) {
                        if (event.isRightClick()) {
                            player.openInventory(new GUIManager().crateInhalt(crate));
                        }else if(event.isLeftClick()){
                            new CrateOpener(crate, player);
                        }
                    }
                }
            }
        }
        if(title.contains("ɪɴʜᴀʟᴛ ᴠᴏᴍ") && title.contains("ᴄʀᴀᴛᴇ") && !title.contains("ʙᴇᴀʀʙᴇɪᴛᴇɴ")) {
            event.setCancelled(true);
        }
    }

}
