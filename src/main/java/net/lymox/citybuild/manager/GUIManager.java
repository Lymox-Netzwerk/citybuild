package net.lymox.citybuild.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.objects.crates.Crate;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.manager.objects.shop.ShopItem;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.ItemCreator;
import net.lymox.citybuild.utils.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    public Inventory editCrate(Crate crate){
        Inventory inventory = Bukkit.createInventory(null, 9, MiniMessage.miniMessage().deserialize("<white>ᴄʀᴀᴛᴇ "+ crate.getName() +" ʙᴇᴀʀʙᴇɪᴛᴇɴ"));

        inventory.setItem(1, new ItemCreator(Material.CHEST).displayName(MiniMessage.miniMessage().deserialize("<gray>ɪɴʜᴀʟᴛ ʙᴇᴀʀʙᴇɪᴛᴇɴ")).build());
        inventory.setItem(4, new ItemCreator(Material.LEVER).displayName(MiniMessage.miniMessage().deserialize("<gray>ᴠᴇʀᴋᴀᴜғ")).lore(MiniMessage.miniMessage().deserialize("<white>➥ " + (crate.isSellable()?"<green>✔":"<red>✘"))).build());
        inventory.setItem(7, new ItemCreator(Material.GOLD_INGOT).displayName(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: " + crate.getPrice())).lore(MiniMessage.miniMessage().deserialize("<white>Linksklick zum erhöhen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum verringern")).build());

        return inventory;
    }

    public Inventory editCrateInhalt(Crate crate){
        Inventory inventory = Bukkit.createInventory(null, 9*5, MiniMessage.miniMessage().deserialize("<white>ɪɴʜᴀʟᴛ ᴠᴏᴍ ᴄʀᴀᴛᴇ "+ crate.getName() +" ʙᴇᴀʀʙᴇɪᴛᴇɴ"));
        for (ItemStack item : crate.getItems()) {
            inventory.addItem(item);
        }
        return inventory;
    }

    public Inventory crateInhalt(Crate crate){
        String name = crate.getName();
        name = name.replace("&", "§");
        Inventory inventory = Bukkit.createInventory(null, 9*5, "ɪɴʜᴀʟᴛ ᴠᴏᴍ ᴄʀᴀᴛᴇ "+ name);
        for (ItemStack item : crate.getItems()) {
            inventory.addItem(item);
        }
        return inventory;
    }

    public Inventory openCratesMenu(Player player){
        Userdata userdata = new Userdata(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, 9*5, MiniMessage.miniMessage().deserialize("<gradient:#ffff55:#c9c943:#a1a136><b>ᴄʀᴀᴛᴇs"));

        ItemStack placeholder = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).displayName(Component.empty()).build();
        for(int i = 0; i <= 8; i++){
            inventory.setItem(i, placeholder);
        }
        inventory.setItem(9, placeholder);
        inventory.setItem(17, placeholder);
        inventory.setItem(18, placeholder);
        inventory.setItem(26, placeholder);
        inventory.setItem(27, placeholder);
        inventory.setItem(35, placeholder);
        for(int i = 36; i <= 44; i++){
            inventory.setItem(i, placeholder);
        }

        int i = 1;
        for (net.lymox.citybuild.utils.userdata.Crate crateH : userdata.getCrates()) {
            if(i < 6) {
                Crate crate = CitybuildPlugin.getInstance().getManagers().getCratesManager().getCrate(crateH.getId());
                String name = crate.getName();
                name = name.replace("&", "§");
                inventory.setItem(slot(i, userdata), new ItemCreator(Material.PAPER).displayName(name).lore(MiniMessage.miniMessage().deserialize("<gray>ᴀɴᴢᴀʜʟ: " + crateH.getAmount()), MiniMessage.miniMessage().deserialize("<white>Linksklick zum Benutzen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum Einsehen der Inhalte")).add("crate", "" + crate.getId()).build());
                i++;
            }
        }

        return inventory;
    }

    private int slot(int i, Userdata userdata){
        CratesManager cratesManager = CitybuildPlugin.getInstance().getManagers().getCratesManager();
        List<Crate> crates = new ArrayList<>();
        for (Crate crate : cratesManager.getCrates()) {
            if(!crate.getItems().isEmpty()){
                if(userdata.getCrate(crate.getId())!=null){
                    if(userdata.getCrate(crate.getId()).getAmount()>0) {
                        crates.add(crate);
                    }
                }
            }
        }

        if(crates.size()==1) {
            return 22;
        }else if(crates.size()==2){
            if(i == 1){
                return 21;
            }else if(i == 2){
                return 23;
            }
        }else if(crates.size()==3){
            if(i == 1){
                return 20;
            }else if(i == 2){
                return 22;
            }else if(i == 3){
                return 24;
            }
        }else if(crates.size()==4){
            if(i == 1){
                return 20;
            }else if(i == 2){
                return 24;
            }else if(i == 3){
                return 13;
            }else if(i == 4){
                return 31;
            }
        }else if(crates.size()==5){
            if(i == 1){
                return 19;
            }else if(i == 2){
                return 21;
            }else if(i == 3){
                return 22;
            }else if(i == 4){
                return 23;
            }else if(i == 5){
                return 25;
            }
        }else if(crates.size()==6){
            if(i == 1){
                return 20;
            }else if(i == 2){
                return 24;
            }else if(i == 3){
                return 12;
            }else if(i == 4){
                return 14;
            }else if(i == 5){
                return 30;
            }else if(i == 6){
                return 31;
            }
        }

        return 0;
    }

    public Inventory editCategorie(Categorie categorie){
        Inventory inventory = Bukkit.createInventory(null, 9*6, "ʙᴇᴀʀʙᴇɪᴛᴇ ᴋᴀᴛᴇɢᴏʀɪᴇ " + categorie.getName());

        for(int i = 45; i <= 53; i++){
            inventory.setItem(i, new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).displayName(Component.empty()).build());
        }

        inventory.setItem(49, new ItemCreator(Material.GREEN_DYE).displayName(MiniMessage.miniMessage().deserialize("<green>Gegenstand hinzufügen")).build());

        int count = 0;
        for (ShopItem item : categorie.getItems()) {
            count+=1;
            if(count < 45){
                ItemStack itemStack = item.getItemStack();
                ItemMeta itemMeta = itemStack.getItemMeta();
                ArrayList<Component> lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<white>ᴘʀᴇɪs: <yellow>" + item.getPrice() + " ᴍüɴᴢᴇɴ"));
                lore.add(MiniMessage.miniMessage().deserialize("<white>sʟᴏᴛ: <gray>" + (item.getInventorySlot()+1)));
                lore.add(MiniMessage.miniMessage().deserialize("<white>Linksklick zum bearbeiten"));
                itemMeta.lore(lore);
                itemStack.setItemMeta(itemMeta);
                inventory.addItem(itemStack);
            }
        }

        return inventory;
    }

    public Inventory addItemToCategorie(Categorie categorie){
        Inventory inventory = Bukkit.createInventory(null, 9*2, categorie.getName()+" ɢᴇɢᴇɴsᴛᴀɴᴅ ʜɪɴᴢᴜғüɢᴇɴ");

        for(int i = 0; i <= 17; i++){
            if(i != 2)
                inventory.setItem(i, new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).displayName(Component.empty()).build());
        }

        inventory.setItem(6, new ItemCreator(Material.GOLD_INGOT).displayName(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: 0")).lore(MiniMessage.miniMessage().deserialize("<white>Linksklick zum erhöhen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum verringern")).build());

        inventory.setItem(17, new ItemCreator(Material.GREEN_DYE).displayName(MiniMessage.miniMessage().deserialize("<green>Hinzufügen")).build());
        return inventory;
    }

    public Inventory openShop(Categorie categorie) {
        ShopManager shopManager = CitybuildPlugin.getInstance().getManagers().getShopManager();
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lShop §8● §f" + categorie.getName());

        // Kategorien unten im Menü anzeigen (maximal 9 Stück)
        List<Categorie> categories = shopManager.getCategories();
        int startSlot = 45;
        for (int j = 0; j < categories.size() && j < 9; j++) {
            Categorie cate = categories.get(j);
            if (cate != null && cate.getMaterial() != null) {
                boolean selected = categorie.getId() == cate.getId();
                Component name = MiniMessage.miniMessage().deserialize((selected ? "<white><b>" : "<white>") + cate.getName());
                ItemStack item = new ItemCreator(cate.getMaterial()).displayName(name).build();
                inventory.setItem(startSlot + j, item);
            }
        }

        for(int i = 36; i <= 44; i++){
            inventory.setItem(i, new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).displayName(Component.empty()).build());
        }

        for(int i = 45; i <= 53; i++){
            if(inventory.getItem(i)==null||inventory.getItem(i).getType().equals(Material.AIR))
                inventory.setItem(i, new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).displayName(Component.empty()).build());
        }

        // Items der aktuell gewählten Kategorie anzeigen
        for (ShopItem shopItem : categorie.getItems()) {
            ItemStack itemStack = shopItem.getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<Component> lore = new ArrayList<>();
            lore.add(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: " + shopItem.getPrice()));
            lore.add(MiniMessage.miniMessage().deserialize("<white>ʟɪɴᴋᴋʟɪᴄᴋ ᴢᴜᴍ ᴋᴀᴜғᴇɴ"));
            itemMeta.lore(lore);
            itemStack.setItemMeta(itemMeta);

            int slot = shopItem.getInventorySlot();
            if (slot >= 0 && slot < 35) {
                inventory.setItem(slot, itemStack);
            }
        }

        return inventory;
    }

}
