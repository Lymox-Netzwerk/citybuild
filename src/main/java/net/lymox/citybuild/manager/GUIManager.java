package net.lymox.citybuild.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPermsProvider;
import net.lymox.citybuild.commands.SkillsCommand;
import net.lymox.citybuild.listeners.storage.StorageClickListener;
import net.lymox.citybuild.manager.objects.crates.Crate;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.manager.objects.shop.ShopItem;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.ItemCreator;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Holzfäller;
import net.lymox.citybuild.utils.userdata.skills.Jäger;
import net.lymox.citybuild.utils.userdata.skills.Miner;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import net.lymox.citybuild.utils.userdata.skills.interfaces.Skill;
import net.lymox.citybuild.utils.userdata.storage.Storage;
import net.lymox.core.master.manager.PermissionManager;
import net.royawesome.jlibnoise.module.combiner.Min;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        for (Crate crate : CitybuildPlugin.getInstance().getManagers().getCratesManager().getCrates()) {
            net.lymox.citybuild.utils.userdata.Crate crateH = userdata.getCrate(crate.getId());
            if(i < 6) {
                String name = crate.getName();
                name = name.replace("&", "§");
                inventory.setItem(slot(i, userdata), new ItemCreator(Material.PAPER).displayName(name).lore(MiniMessage.miniMessage().deserialize("<gray>ᴀɴᴢᴀʜʟ: " + (crateH==null?"0":""+crateH.getAmount())), MiniMessage.miniMessage().deserialize("<white>Linksklick zum Benutzen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum Einsehen der Inhalte")).add("crate", "" + crate.getId()).build());
                i++;
            }
        }

//        for (net.lymox.citybuild.utils.userdata.Crate crateH : userdata.getCrates()) {
//            if(i < 6) {
//                Crate crate = CitybuildPlugin.getInstance().getManagers().getCratesManager().getCrate(crateH.getId());
//                String name = crate.getName();
//                name = name.replace("&", "§");
//                inventory.setItem(slot(i, userdata), new ItemCreator(Material.PAPER).displayName(name).lore(MiniMessage.miniMessage().deserialize("<gray>ᴀɴᴢᴀʜʟ: " + crateH.getAmount()), MiniMessage.miniMessage().deserialize("<white>Linksklick zum Benutzen"), MiniMessage.miniMessage().deserialize("<white>Rechtsklick zum Einsehen der Inhalte")).add("crate", "" + crate.getId()).build());
//                i++;
//            }
//        }

        return inventory;
    }

    private int slot(int i, Userdata userdata){
        CratesManager cratesManager = CitybuildPlugin.getInstance().getManagers().getCratesManager();
        //        for (Crate crate : cratesManager.getCrates()) {
//            if(!crate.getItems().isEmpty()){
//                if(userdata.getCrate(crate.getId())!=null){
//                    if(userdata.getCrate(crate.getId()).getAmount()>0) {
//                        crates.add(crate);
//                    }
//                }
//            }
//        }
        List<Crate> crates = new ArrayList<>(cratesManager.getCrates());

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

    public Inventory openShop(Categorie categorie, boolean isCrates) {
        ShopManager shopManager = CitybuildPlugin.getInstance().getManagers().getShopManager();
        CratesManager cratesManager = CitybuildPlugin.getInstance().getManagers().getCratesManager();
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lShop §8● §f" + (isCrates?"Crates":categorie.getName()));

        // Kategorien unten im Menü anzeigen (maximal 9 Stück)
        List<Categorie> categories = shopManager.getCategories();
        int startSlot = 45;
        int lastSlot = 0;
        for (int j = 0; j < categories.size() && j < 9; j++) {
            Categorie cate = categories.get(j);
            if (cate != null && cate.getMaterial() != null) {
                boolean selected = categorie.getId() == cate.getId();
                if(isCrates){
                    selected = false;
                }
                Component name = MiniMessage.miniMessage().deserialize((selected ? "<white><b>" : "<white>") + cate.getName());
                ItemStack item = new ItemCreator(cate.getMaterial()).displayName(name).build();
                inventory.setItem(startSlot + j, item);
                lastSlot = startSlot + j;
            }
        }

        if(!cratesManager.getSellableCrates().isEmpty()){
            lastSlot=lastSlot+1;
            Component name = MiniMessage.miniMessage().deserialize((isCrates ? "<white><b>" : "<white>") + "Crates");
            ItemStack item = new ItemCreator(Material.PAPER).displayName(name).build();
            inventory.setItem(lastSlot, item);
        }

        for(int i = 36; i <= 44; i++){
            inventory.setItem(i, new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).displayName(Component.empty()).build());
        }

        for(int i = 45; i <= 53; i++){
            if(inventory.getItem(i)==null||inventory.getItem(i).getType().equals(Material.AIR))
                inventory.setItem(i, new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).displayName(Component.empty()).build());
        }

        // Items der aktuell gewählten Kategorie anzeigen
        if(isCrates){
            int slot = 0;
            for (Crate sellableCrate : cratesManager.getSellableCrates()) {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();

                String name = sellableCrate.getName();
                name = name.replace("&", "§");

                itemMeta.setDisplayName(name);

                List<Component> lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: " + (sellableCrate.getPrice()==0?"Kostenlos":""+sellableCrate.getPrice())));
                lore.add(MiniMessage.miniMessage().deserialize("<white>ʟɪɴᴋᴋʟɪᴄᴋ ᴢᴜᴍ ᴋᴀᴜғᴇɴ"));
                itemMeta.lore(lore);
                itemStack.setItemMeta(itemMeta);
                if (slot >= 0 && slot < 35) {
                    inventory.setItem(slot, itemStack);
                    slot++;
                }
            }
        }else {
            for (ShopItem shopItem : categorie.getItems()) {
                ItemStack itemStack = shopItem.getItemStack();
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<Component> lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<gray>ᴘʀᴇɪs: " + (shopItem.getPrice()==0?"Kostenlos":""+shopItem.getPrice())));
                lore.add(MiniMessage.miniMessage().deserialize("<white>ʟɪɴᴋᴋʟɪᴄᴋ ᴢᴜᴍ ᴋᴀᴜғᴇɴ"));
                itemMeta.lore(lore);
                itemStack.setItemMeta(itemMeta);

                int slot = shopItem.getInventorySlot();
                if (slot >= 0 && slot < 35) {
                    inventory.setItem(slot, itemStack);
                }
            }
        }

        return inventory;
    }

    public Inventory openStorage(UUID uuid, int id){
        Userdata userdata = new Userdata(uuid);
        if(id == 0){
            Inventory inventory = Bukkit.createInventory(null, 9, "§a§lDeine Storages");
            int i = 1;
            for (Storage storage : userdata.getStorages()) {
                ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                if(storage.isBought()|| StorageClickListener.hasStorage(uuid, i)){
                    itemStack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.displayName(MiniMessage.miniMessage().deserialize(i +". sᴛᴏʀᴀɢᴇ"));
                List<Component> lore = new ArrayList<>();
                if(storage.isBought()|| StorageClickListener.hasStorage(uuid, i)){
                    lore.add(MiniMessage.miniMessage().deserialize("<green>Freigeschaltet"));
                }else {
                    boolean canBeBought = false;
                    if(i <= 2){
                        canBeBought = true;
                    }else {
                        if(userdata.getStorage(i-1).isBought()){
                            canBeBought = true;
                        }
                    }
                    if(canBeBought){
                        lore.add(MiniMessage.miniMessage().deserialize("<red>Kaufen für X"));
                    }else {
                        lore.add(MiniMessage.miniMessage().deserialize("<red>Kaufe erst den " +(i-1)+ ". sᴛᴏʀᴀɢᴇ"));
                    }
                }
                itemMeta.lore(lore);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i-1, itemStack);
                i++;
            }
            return inventory;
        }
        return null;
    }

    public Inventory openSkills(UUID uuid, SkillType skillType){
        Userdata userdata = new Userdata(uuid);
        if(skillType==null){
            Inventory inventory = Bukkit.createInventory(null, 9, "§a§lsᴋɪʟʟs");

            if(userdata.getSkill(SkillType.MONSTERJÄGER)!=null){
                Monsterjäger monsterjäger = (Monsterjäger) userdata.getSkill(SkillType.MONSTERJÄGER);

                int newLevel = monsterjäger.getLevel()+1;

                int exp = monsterjäger.getExp() - monsterjäger.requiredExp(newLevel-1);
                int newexp = monsterjäger.requiredExp((newLevel)) - monsterjäger.requiredExp(newLevel-1);

                double fortschrittProzent = ((double) exp / newexp) * 100;

                List<Component> lore = new ArrayList<>();

                if(monsterjäger.getLevel()==10){
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + 10 + ": <yellow>100%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(100) + " <yellow>" + monsterjäger.requiredExp(10) + "<gold>/<yellow>" + monsterjäger.requiredExp(10)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + 10));
                    lore.addAll(monsterjäger.rewards(10, true));
                }else {
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + newLevel + ": <yellow>" + Math.round(fortschrittProzent) + "%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(Math.round(fortschrittProzent)) + " <yellow>" + monsterjäger.getExp() + "<gold>/<yellow>" + monsterjäger.requiredExp(newLevel)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + newLevel));
                    lore.addAll(monsterjäger.rewards(newLevel, false));
                }

                inventory.setItem(0, new ItemCreator(Material.CREEPER_HEAD).
                        displayName(MiniMessage.miniMessage().deserialize("<color:#54c571>Monsterjäger</color>"))
                        .lore(lore)
                        .build());
            }

            if(userdata.getSkill(SkillType.JÄGER)!=null){
                Jäger jäger = (Jäger) userdata.getSkill(SkillType.JÄGER);

                int newLevel = jäger.getLevel()+1;

                int exp = jäger.getExp() - jäger.requiredExp(newLevel-1);
                int newexp = jäger.requiredExp((newLevel)) - jäger.requiredExp(newLevel-1);

                double fortschrittProzent = ((double) exp / newexp) * 100;

                List<Component> lore = new ArrayList<>();

                if(jäger.getLevel()==10){
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + 10 + ": <yellow>100%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(100) + " <yellow>" + jäger.requiredExp(10) + "<gold>/<yellow>" + jäger.requiredExp(10)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + 10));
                    lore.addAll(jäger.rewards(10, true));
                }else {
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + newLevel + ": <yellow>" + Math.round(fortschrittProzent) + "%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(Math.round(fortschrittProzent)) + " <yellow>" + jäger.getExp() + "<gold>/<yellow>" + jäger.requiredExp(newLevel)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + newLevel));
                    lore.addAll(jäger.rewards(newLevel, false));
                }

                inventory.setItem(1, new ItemCreator(Material.STONE_SWORD).
                        displayName(MiniMessage.miniMessage().deserialize("<green>Jäger</green>"))
                        .lore(lore)
                        .build());
            }

            if(userdata.getSkill(SkillType.MINER)!=null){
                Miner miner = (Miner) userdata.getSkill(SkillType.MINER);

                int newLevel = miner.getLevel()+1;

                int exp = miner.getExp() - miner.requiredExp(newLevel-1);
                int newexp = miner.requiredExp((newLevel)) - miner.requiredExp(newLevel-1);

                double fortschrittProzent = ((double) exp / newexp) * 100;

                List<Component> lore = new ArrayList<>();

                if(miner.getLevel()==10){
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + 10 + ": <yellow>100%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(100) + " <yellow>" + miner.requiredExp(10) + "<gold>/<yellow>" + miner.requiredExp(10)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + 10));
                    lore.addAll(miner.rewards(10, true));
                }else {
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + newLevel + ": <yellow>" + Math.round(fortschrittProzent) + "%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(Math.round(fortschrittProzent)) + " <yellow>" + miner.getExp() + "<gold>/<yellow>" + miner.requiredExp(newLevel)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + newLevel));
                    lore.addAll(miner.rewards(newLevel, false));
                }

                inventory.setItem(2, new ItemCreator(Material.STONE_PICKAXE).
                        displayName(MiniMessage.miniMessage().deserialize("<color:#9e9e35>Minenarbeiter</color>"))
                        .lore(lore)
                        .build());
            }

            if(userdata.getSkill(SkillType.HOLZFÄLLER)!=null){
                Holzfäller holzfäller = (Holzfäller) userdata.getSkill(SkillType.HOLZFÄLLER);

                int newLevel = holzfäller.getLevel()+1;

                int exp = holzfäller.getExp() - holzfäller.requiredExp(newLevel-1);
                int newexp = holzfäller.requiredExp((newLevel)) - holzfäller.requiredExp(newLevel-1);

                double fortschrittProzent = ((double) exp / newexp) * 100;

                List<Component> lore = new ArrayList<>();

                if(holzfäller.getLevel()==10){
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + 10 + ": <yellow>100%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(100) + " <yellow>" + holzfäller.requiredExp(10) + "<gold>/<yellow>" + holzfäller.requiredExp(10)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + 10));
                    lore.addAll(holzfäller.rewards(10, true));
                }else {
                    lore.add(MiniMessage.miniMessage().deserialize("<white>ғᴏʀᴛsᴄʜʀɪᴛᴛ ғüʀ ʟᴇᴠᴇʟ " + newLevel + ": <yellow>" + Math.round(fortschrittProzent) + "%"));
                    lore.add(MiniMessage.miniMessage().deserialize(SkillsCommand.getBalken(Math.round(fortschrittProzent)) + " <yellow>" + holzfäller.getExp() + "<gold>/<yellow>" + holzfäller.requiredExp(newLevel)));
                    lore.add(Component.empty());
                    lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ ғüʀ ʟᴇᴠᴇʟ " + newLevel));
                    lore.addAll(holzfäller.rewards(newLevel, false));
                }

                inventory.setItem(3, new ItemCreator(Material.STONE_AXE).
                        displayName(MiniMessage.miniMessage().deserialize("<color:#cf5c00>Holzfäller</color>"))
                        .lore(lore)
                        .build());
            }

            return inventory;
        }else {
            Skill skill =  userdata.getSkill(skillType);
            Inventory inventory = Bukkit.createInventory(null, 9*3, "§a§lsᴋɪʟʟs §8● " + skill.getColor() + skill.getName());

            for(int i = 0; i<inventory.getSize(); i++){
                inventory.setItem(i, new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).displayName(Component.empty()).build());
            }

            for(int i = 0; i<=9; i++){
                int level = i+1;
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                String color = "<red>";
                if(skill.getLevel()==level&&skill.getLevel()<10){
                    itemStack.setType(Material.ORANGE_STAINED_GLASS_PANE);
                    color = "<gold>";
                }else if(skill.getLevel()>level || skill.getLevel()>=10){
                    itemStack.setType(Material.LIME_STAINED_GLASS_PANE);
                    color = "<green>";
                }
                itemMeta.displayName(MiniMessage.miniMessage().deserialize(color+skill.getNameFormatted()+" " + level));
                List<Component> lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<gray>ʙᴇʟᴏʜɴᴜɴɢᴇɴ"));
                lore.addAll(skill.rewards(level, skill.getLevel() == 10 || skill.getLevel() > level));
                itemMeta.lore(lore);
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(SkillsCommand.slot(level), itemStack);
            }
            return inventory;
        }
    }

}
