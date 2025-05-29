package net.lymox.citybuild.listeners.storage;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPermsProvider;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.utils.userdata.skills.Monsterjäger;
import net.lymox.citybuild.utils.userdata.skills.enums.SkillType;
import net.lymox.citybuild.utils.userdata.storage.Storage;
import net.lymox.core.master.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StorageClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType().equals(Material.AIR))
            return;
        if (event.getView().title() == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if(title.equalsIgnoreCase("Deine Storages")){
            event.setCancelled(true);
            Userdata userdata = new Userdata(player.getUniqueId());
            int id = event.getRawSlot()+1;
            Storage storage = userdata.getStorage(id);
            if(storage.isBought() || hasStorage(player.getUniqueId(), id)){
                Inventory inventory = Bukkit.createInventory(null, 9*6, storage.getId() + ". sᴛᴏʀᴀɢᴇ");
                if(storage.getId() == 1){
                    inventory = Bukkit.createInventory(null, 9*4, storage.getId() + ". sᴛᴏʀᴀɢᴇ");
                }
                if(storage.getContents()!=null){
                    inventory.setContents(storage.getContents());
                }
                player.openInventory(inventory);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if(title.contains("sᴛᴏʀᴀɢᴇ")){
            String[] titleRaw = title.split(" ");
            String idStr = titleRaw[0];
            idStr=idStr.replace(".", "");
            idStr=idStr.replace("[", "");
            int id = Integer.parseInt(idStr);
            if(id > 0){
                Userdata userdata = new Userdata(player.getUniqueId());
                Storage storage = userdata.getStorage(id);
                storage.setContents(event.getInventory().getContents());
                userdata.saveStorage(storage);
            }
        }
    }

    public static boolean hasStoragePermission(UUID uuid, int storage){
        for(int i = 2; i<=9; i++){
            if(new PermissionManager(LuckPermsProvider.get()).hasPermission(uuid, "lymox.citybuild.storages."+i)){
                if(storage <= i){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasStorage(UUID uuid, int storage){
        Userdata userdata = new Userdata(uuid);
        List<Integer> opend = new ArrayList<>();
        for (Storage userdataStorage : userdata.getStorages()) {
            if(userdataStorage.isBought()){
                opend.add(userdataStorage.getId());
            }else {
                if(hasStoragePermission(uuid, userdataStorage.getId())){
                    opend.add(userdataStorage.getId());
                }
            }
        }
        int additional = 0;
        if(userdata.getSkill(SkillType.MONSTERJÄGER).getLevel()>=5){
            additional++;
        }
        if(userdata.getSkill(SkillType.JÄGER).getLevel()>=5){
            additional++;
        }
        if(opend.contains(storage)){
            return true;
        }else {
            int highest = Collections.max(opend);
            int result = highest+additional;
            return result >= storage;
        }
    }

    public static boolean hasStorageAccessThruSkill(UUID uuid, int storage){
        int opendStashes = 0;

        Userdata userdata = new Userdata(uuid);
        if(userdata.getSkill(SkillType.MONSTERJÄGER).getLevel()>=5){
            opendStashes++;
        }

        int alreadyBought = 0;
        for (Storage userdataStorage : userdata.getStorages()) {
            if(userdataStorage.isBought()){
                alreadyBought++;
            }
        }

        for(int i = 2; i<=9; i++){
            if(new PermissionManager(LuckPermsProvider.get()).hasPermission(uuid, "lymox.citybuild.storages."+i)){
                if(storage <= i){
                    return true;
                }
            }
        }
        return false;
    }

}
