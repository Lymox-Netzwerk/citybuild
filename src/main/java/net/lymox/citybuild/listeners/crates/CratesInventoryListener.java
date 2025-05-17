package net.lymox.citybuild.listeners.crates;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.manager.CratesManager;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.Crate;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CratesInventoryListener implements Listener {

    private CratesManager cratesManager = CitybuildPlugin.getInstance().getManagers().getCratesManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player))return;
        if(event.getClickedInventory()==null)return;
        if(event.getCurrentItem()==null)return;
        if(event.getCurrentItem().getType().equals(Material.AIR)||event.getCurrentItem().getItemMeta()==null||event.getCurrentItem().getItemMeta().displayName()==null)return;
        if(event.getView().title()==null)return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if(title.contains("ᴄʀᴀᴛᴇ") && title.contains("ʙᴇᴀʀʙᴇɪᴛᴇɴ")){
            String[] titleSpl = title.split(" ");
            Crate crate = cratesManager.getCrate(titleSpl[1]);
            event.setCancelled(true);

            if(crate!=null) {
                if (event.getRawSlot() == 1) {
                    player.openInventory(new GUIManager().editCrateInhalt(crate));
                }
                if (event.getRawSlot() == 4) {
                    if(crate.isSellable()){
                        crate.setSellable(false);
                    }else {
                        crate.setSellable(true);
                    }
                    cratesManager.saveCrate(crate);
                    player.openInventory(new GUIManager().editCrate(crate));
                }
                if (event.getRawSlot() == 7) {
                    if(event.isLeftClick()){
                        int i = 1;
                        if(event.isShiftClick()){
                            i = 10;
                        }
                        crate.setPrice(crate.getPrice()+i);
                        cratesManager.saveCrate(crate);
                        player.openInventory(new GUIManager().editCrate(crate));
                    }else if(event.isRightClick()){
                        int i = 1;
                        if(event.isShiftClick()){
                            i = 10;
                        }
                        if(crate.getPrice()<i){
                            crate.setPrice(0);
                        }else {
                            crate.setPrice(crate.getPrice()-i);
                        }
                        cratesManager.saveCrate(crate);
                        player.openInventory(new GUIManager().editCrate(crate));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getView().title()==null)return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if(title.contains("ɪɴʜᴀʟᴛ ᴠᴏᴍ") && title.contains("ᴄʀᴀᴛᴇ") && title.contains("ʙᴇᴀʀʙᴇɪᴛᴇɴ")) {
            String[] titleSpl = title.split(" ");
            Crate crate = cratesManager.getCrate(titleSpl[3]);
            if(crate!=null){
                crate.getItems().clear();
                for (@Nullable ItemStack content : event.getInventory().getContents()) {
                    if(content != null && !content.getType().equals(Material.AIR)) {
                        crate.getItems().add(content);
                    }
                }
                cratesManager.saveCrate(crate);
                event.getPlayer().sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Inhalt gespeichert")));
            }
        }

    }


}
