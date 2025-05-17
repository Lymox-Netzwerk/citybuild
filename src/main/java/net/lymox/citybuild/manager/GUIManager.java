package net.lymox.citybuild.manager;

import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.objects.Crate;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.ItemCreator;
import net.lymox.citybuild.utils.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

}
