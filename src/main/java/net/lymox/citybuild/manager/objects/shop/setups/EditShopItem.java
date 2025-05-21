package net.lymox.citybuild.manager.objects.shop.setups;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.manager.objects.shop.ShopItem;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditShopItem implements Listener {

    private Categorie categorie;
    private ShopItem shopItem;
    private Inventory inventory;
    private Player player;
    private boolean clickCooldown;

    public EditShopItem(Categorie categorie, ShopItem shopItem, Player player) {
        this.categorie = categorie;
        this.shopItem = shopItem;
        this.inventory = Bukkit.createInventory(null, 9, "");
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, CitybuildPlugin.getInstance());
        clickCooldown = false;
        openInventory();
    }

    public void openInventory(){
        inventory.clear();
        inventory.setItem(4, new ItemCreator(Material.GOLD_INGOT).displayName(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: " + shopItem.getPrice())).lore(MiniMessage.miniMessage().deserialize("<white>Linksklick zum erhöhen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum verringern")).build());
        inventory.setItem(1, new ItemCreator(Material.CHEST).displayName(MiniMessage.miniMessage().deserialize("<gray>sʟᴏᴛ: " + (shopItem.getInventorySlot()+1))).lore(MiniMessage.miniMessage().deserialize("<white>Linksklick zum erhöhen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum verringern")).build());
        inventory.setItem(7, new ItemCreator(Material.BARRIER).displayName(MiniMessage.miniMessage().deserialize("<red>Löschen")).lore(MiniMessage.miniMessage().deserialize("<white>Linksklick")).build());
        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player player))return;
        if(event.getClickedInventory().equals(inventory)){
            event.setCancelled(true);
        }
        if(event.getRawSlot()==1){
            if(clickCooldown)return;
            clickCooldown = true;

            Bukkit.getScheduler().runTaskLater(CitybuildPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if(clickCooldown)
                        clickCooldown=false;
                }
            },2);
            ItemStack itemStack = event.getCurrentItem();
            String itemnameRaw = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());
            itemnameRaw = itemnameRaw.replace("[", "");
            itemnameRaw = itemnameRaw.replace("]", "");
            String[] itemname = itemnameRaw.split(" ");
            int amount = Integer.parseInt(itemname[1]);
            if(event.isLeftClick()){
                amount+=1;
            }else if(event.isRightClick()){
                if(amount > 1){
                    amount-=1;
                }
            }
            ItemMeta meta = itemStack.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize("<gray>sʟᴏᴛ: "+amount));
            itemStack.setItemMeta(meta);
            event.getClickedInventory().setItem(1, itemStack);
            player.updateInventory();

            for (ShopItem item : categorie.getItems()) {
                if(item.getInventorySlot()==(amount-1)){
                    item.setInventorySlot(shopItem.getInventorySlot());
                }
            }
            shopItem.setInventorySlot(amount-1);
            categorie.save();
        }
        if(event.getRawSlot()==4){
            if(clickCooldown)return;
            clickCooldown = true;

            Bukkit.getScheduler().runTaskLater(CitybuildPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if(clickCooldown)
                        clickCooldown=false;
                }
            },2);

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
            event.getClickedInventory().setItem(4, itemStack);
            player.updateInventory();
            shopItem.setPrice(amount);
            categorie.save();
        }
        if(event.getRawSlot()==7){
            if(clickCooldown)return;
            clickCooldown = true;

            Bukkit.getScheduler().runTaskLater(CitybuildPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if(clickCooldown)
                        clickCooldown=false;
                }
            },2);
            categorie.getItems().remove(shopItem);
            categorie.save();
            player.openInventory(new GUIManager().editCategorie(categorie));
        }
    }

}
