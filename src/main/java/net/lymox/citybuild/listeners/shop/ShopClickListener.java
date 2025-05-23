package net.lymox.citybuild.listeners.shop;

import io.lumine.mythic.bukkit.utils.lib.jooq.User;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.manager.CratesManager;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.ShopManager;
import net.lymox.citybuild.manager.objects.crates.Crate;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.manager.objects.shop.ShopItem;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import net.lymox.citybuild.utils.Userdata;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ShopClickListener implements Listener {

    private ShopManager shopManager = CitybuildPlugin.getInstance().getManagers().getShopManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType().equals(Material.AIR))
            return;
        if (event.getView().title() == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (title.contains("Shop ●")) {
            event.setCancelled(true);

            if(event.getRawSlot()>=45&&event.getRawSlot()<=53){
                if(!event.getCurrentItem().getType().equals(Material.WHITE_STAINED_GLASS_PANE)){
                    if(event.getCurrentItem().getItemMeta().displayName()!=null){
                        String name = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
                        if(event.getCurrentItem().getType().equals(Material.PAPER) && name.equalsIgnoreCase("Crates")){
                            player.openInventory(new GUIManager().openShop(shopManager.getCategories().getFirst(), true));
                            return;
                        }
                        Categorie categorie = shopManager.getCategorie(name);
                        if(categorie != null){
                            player.openInventory(new GUIManager().openShop(categorie, false));
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,1,1);
                        }
                    }
                }
            }

            if(event.getRawSlot()>=0&&event.getRawSlot()<=34){
                String[] titleRaw = title.split(" ");
                String categorieName = titleRaw[2];

                if(categorieName.equalsIgnoreCase("Crates")){
                    if(event.getCurrentItem().getType().equals(Material.PAPER)){
                    String name = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
                    Crate crate = CitybuildPlugin.getInstance().getManagers().getCratesManager().getSellableCrates().get(event.getRawSlot());
                    if(crate!=null){
                        System.out.println("Found");
                        Userdata userdata = new Userdata(player.getUniqueId());
                        if(userdata.getMünzen()>=crate.getPrice()){
                            if (player.getInventory().firstEmpty() == -1) {
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Dein Inventar ist zu voll, um diesen Gegenstand zu kaufen.")));
                                return;
                            }
                            userdata.setMünzen(userdata.getMünzen()- crate.getPrice());
                            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Der Kauf von <b>1x " + name + "</b> war <green>erfolgreich<gray>.")));
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                            userdata.addCrate(crate.getId(), 1);
                        }else {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                            String message = Messages.notEnaughCoins;
                            int amount = crate.getPrice()-userdata.getMünzen();
                            message = message.replace("%c", ""+amount);
                            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
                        }
                    }
                    }
                }else {
                    Categorie categorie = shopManager.getCategorie(categorieName);
                    if(categorie!=null){
                        ShopItem shopItem = null;
                        for (ShopItem item : categorie.getItems()) {
                            if(item.getInventorySlot()==event.getRawSlot()){
                                shopItem = item;
                            }
                        }
                        if(shopItem != null){
                            ItemStack itemStack = shopItem.getItemStack();
                            Userdata userdata = new Userdata(player.getUniqueId());
                            if(userdata.getMünzen()>=shopItem.getPrice()){
                                if (player.getInventory().firstEmpty() == -1) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Dein Inventar ist zu voll, um diesen Gegenstand zu kaufen.")));
                                    return;
                                }
                                player.getInventory().addItem(itemStack);
                                userdata.setMünzen(userdata.getMünzen()- shopItem.getPrice());
                                if(itemStack.hasItemMeta()&&itemStack.getItemMeta().displayName()!=null) {
                                    String name = PlainTextComponentSerializer.plainText().serialize(itemStack.getItemMeta().displayName());
                                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Der Kauf von <b>" + itemStack.getAmount() + "x " + name + "</b> war <green>erfolgreich<gray>.")));
                                }else {
                                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Der Kauf von <b>" + itemStack.getAmount() + "x " + itemStack.getType() + "</b> war <green>erfolgreich<gray>.")));
                                }
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                            }else {
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                                String message = Messages.notEnaughCoins;
                                int amount = shopItem.getPrice()-userdata.getMünzen();
                                message = message.replace("%c", ""+amount);
                                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
                            }
                        }
                    }
                }

            }

        }
    }


}
