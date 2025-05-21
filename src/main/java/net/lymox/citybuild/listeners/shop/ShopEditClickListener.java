package net.lymox.citybuild.listeners.shop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.commands.ShopCommand;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.manager.objects.shop.ShopItem;
import net.lymox.citybuild.manager.objects.shop.setups.EditShopItem;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopEditClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType().equals(Material.AIR))
            return;
        if (event.getView().title() == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (title.contains("ʙᴇᴀʀʙᴇɪᴛᴇ ᴋᴀᴛᴇɢᴏʀɪᴇ")) {
            event.setCancelled(true);
            String[] titleStr = title.split(" ");
            Categorie categorie = CitybuildPlugin.getInstance().getManagers().getShopManager().getCategorie(titleStr[2]);
            if(categorie!=null){
                if(event.getRawSlot()<45){
                    List<Component> lore = event.getCurrentItem().getItemMeta().lore();
                    for (Component component : lore) {
                        String text = PlainTextComponentSerializer.plainText().serialize(component);
                        if(text.contains("sʟᴏᴛ")){
                            text = text.replace("sʟᴏᴛ: ", "");
                            int slot = Integer.parseInt(text);
                            slot-=1;
                            ShopItem shopItem = null;
                            for (ShopItem item : categorie.getItems()) {
                                if(item.getInventorySlot()==slot){
                                    shopItem = item;
                                }
                            }
                            if(shopItem != null){
                                new EditShopItem(categorie, shopItem, player);
                            }
                        }
                    }
                }
                if(event.getRawSlot()==49){
                    player.openInventory(new GUIManager().addItemToCategorie(categorie));
                }
            }
        }
        if(title.contains("ɢᴇɢᴇɴsᴛᴀɴᴅ ʜɪɴᴢᴜғüɢᴇɴ")){
            if(event.getRawSlot()!=2){
                if(event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())){
                    event.setCancelled(true);
                    String[] titleStr = title.split(" ");
                    String categorieName = titleStr[0];
                    categorieName = categorieName.replace("]", "");
                    System.out.println(categorieName);
                    Categorie categorie = CitybuildPlugin.getInstance().getManagers().getShopManager().getCategorie(categorieName);
                    if(categorie==null){
                        return;
                    }
                    if(event.getRawSlot()==6){
                        ItemStack itemStack = event.getCurrentItem();
                        String itemnameRaw = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());
                        itemnameRaw = itemnameRaw.replace("[", "");
                        itemnameRaw = itemnameRaw.replace("]", "");
                        String[] itemname = itemnameRaw.split(" ");
                        int amount = Integer.parseInt(itemname[1]);
                        if(event.isLeftClick()){
                            if(event.isShiftClick()){
                                amount+=10;
                            }else{
                                amount+=1;
                            }
                        }else if(event.isRightClick()){
                            if(event.isShiftClick()){
                                if(amount<10){
                                    amount=0;
                                }else {
                                    amount-=10;
                                }
                            }else {
                                if(amount > 0){
                                    amount-=1;
                                }
                            }
                        }
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.displayName(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: "+amount));
                        itemStack.setItemMeta(meta);
                        event.getClickedInventory().setItem(6, itemStack);
                        player.updateInventory();
                    }
                    if(event.getRawSlot()==17){
                        ItemStack itemStack = event.getClickedInventory().getItem(2);
                        if(itemStack==null||itemStack.getType().equals(Material.AIR)){
                            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Es muss ein Gegenstand in den leeren Slot eingesetzt werden!")));
                            return;
                        }
                        String itemnameRaw = PlainTextComponentSerializer.plainText().serialize(event.getClickedInventory().getItem(6).displayName());
                        itemnameRaw = itemnameRaw.replace("[", "");
                        itemnameRaw = itemnameRaw.replace("]", "");
                        String[] itemname = itemnameRaw.split(" ");
                        int amount = Integer.parseInt(itemname[1]);
                        categorie.getItems().add(new ShopItem(categorie.getId(), CitybuildPlugin.getInstance().getManagers().getShopManager().getNextInventorySlotID(categorie.getId()), itemStack, amount));
                        categorie.save();
                        player.openInventory(new GUIManager().editCategorie(categorie));
                        player.updateInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player player))return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (title.contains("ʙᴇᴀʀʙᴇɪᴛᴇ ᴋᴀᴛᴇɢᴏʀɪᴇ")) {
            if(ShopCommand.items.containsKey(player)){
                player.getInventory().setContents(ShopCommand.items.get(player));
                ShopCommand.items.remove(player);
            }
            String[] titleStr = title.split(" ");
            Categorie categorie = CitybuildPlugin.getInstance().getManagers().getShopManager().getCategorie(titleStr[2]);
            categorie.save();
        }
    }


}
