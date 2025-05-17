package net.lymox.citybuild.manager.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.ItemCreator;
import net.lymox.citybuild.utils.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class CrateOpener implements Listener {

    private Crate crate;
    private Player player;
    private BukkitTask task;
    Inventory inventory;
    private int delay;
    private int pos;
    private int zycl;

    public CrateOpener(Crate crate, Player player) {
        Bukkit.getPluginManager().registerEvents(this, CitybuildPlugin.getInstance());
        this.crate = crate;
        this.player = player;
        String name = crate.getName();
        name = name.replace("&", "§");
        this.inventory = Bukkit.createInventory(null, 9*3, name);
        this.delay = 1;
        this.pos = 1;
        this.zycl = 1;
        new Userdata(player.getUniqueId()).removeCrate(crate.getId(), 1);
        task();
    }

    public void task(){
        if(zycl == 1) {
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getOpenInventory().getTopInventory() != inventory) {
                        player.openInventory(inventory);
                    }

                    if (getPos() == 1) {
                        for (int i = 0; i <= 8; i++) {
                            inventory.setItem(i, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).displayName(Component.empty()).build());
                        }
                        for (int i = 18; i <= 26; i++) {
                            inventory.setItem(i, new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).displayName(Component.empty()).build());
                        }
                        inventory.setItem(4, new ItemCreator(Material.WHITE_STAINED_GLASS_PANE).displayName(MiniMessage.miniMessage().deserialize("<white>ɢᴇᴡɪɴɴ")).build());
                        for (int i = 9; i <= 17; i++) {
                            inventory.setItem(i, random());
                        }
                    } else {
                        versetzen();
                        inventory.setItem(17, random());
                    }

                    setPos(getPos() + 1);
                    System.out.println(getDelay());

                    if (getPos() == 30) {
                        setDelay(2);
                        task.cancel();
                        zycl=2;
                        task();
                    }

                }
            }.runTaskTimer(CitybuildPlugin.getInstance(), 0, getDelay());
        }
        if(zycl == 2){
            if(task.isCancelled()){
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getOpenInventory().getTopInventory() != inventory) {
                            player.openInventory(inventory);
                        }


                        versetzen();
                        inventory.setItem(17, random());


                        setPos(getPos() + 1);
                        System.out.println(getDelay());

                        if (getPos() == 45) {
                            setDelay(4);
                            zycl+=1;
                            task.cancel();
                            task();
                        }

                    }
                }.runTaskTimer(CitybuildPlugin.getInstance(), 0, getDelay());
            }
        }
        if(zycl == 3){
            if(task.isCancelled()){
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getOpenInventory().getTopInventory() != inventory) {
                            player.openInventory(inventory);
                        }


                        versetzen();
                        inventory.setItem(17, random());


                        setPos(getPos() + 1);

                        if (getPos() == 52) {
                            setDelay(8);
                            task.cancel();
                            zycl+=1;
                            task();
                        }

                    }
                }.runTaskTimer(CitybuildPlugin.getInstance(), 0, getDelay());
            }
        }
        if(zycl == 4){
            if(task.isCancelled()){
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getOpenInventory().getTopInventory() != inventory) {
                            player.openInventory(inventory);
                        }


                        versetzen();
                        inventory.setItem(17, random());


                        setPos(getPos() + 1);

                        if (getPos() == 57) {
                            setDelay(12);
                            task.cancel();
                            zycl+=1;
                            task();
                        }

                    }
                }.runTaskTimer(CitybuildPlugin.getInstance(), 0, getDelay());
            }
        }
        if(zycl == 5){
            if(task.isCancelled()){
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getOpenInventory().getTopInventory() != inventory) {
                            player.openInventory(inventory);
                        }


                        versetzen();
                        inventory.setItem(17, random());


                        setPos(getPos() + 1);

                        if (getPos() == 59) {
                            setDelay(20);
                            task.cancel();
                            zycl+=1;
                            task();
                        }

                    }
                }.runTaskTimer(CitybuildPlugin.getInstance(), 0, getDelay());
            }
        }
        if(zycl == 6){
            if(task.isCancelled()){
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getOpenInventory().getTopInventory() != inventory) {
                            player.openInventory(inventory);
                        }


                        versetzen();
                        inventory.setItem(17, random());


                        setPos(getPos() + 1);

                        if (getPos() == 60) {
                            task.cancel();
                            zycl+=1;
                            task();
                        }

                    }
                }.runTaskTimer(CitybuildPlugin.getInstance(), 20, getDelay());
            }
        }
        if(zycl == 7){
            ItemStack itemStack = inventory.getItem(13);
            assert itemStack != null;
            player.getInventory().addItem(itemStack);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            for (int i = 0; i <= 8; i++) {
                if(i!=4)
                    inventory.setItem(i, new ItemCreator(Material.YELLOW_STAINED_GLASS_PANE).displayName(Component.empty()).build());
            }
            for (int i = 18; i <= 26; i++) {
                inventory.setItem(i, new ItemCreator(Material.YELLOW_STAINED_GLASS_PANE).displayName(Component.empty()).build());
            }
            this.player=null;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory()==inventory)event.setCancelled(true);
    }


    public void versetzen(){
        for(int i = 10; i <= 17; i++){
            ItemStack itemStack = inventory.getItem(i);
            inventory.setItem(i-1, itemStack);
        }
        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK,1,1);
    }

    public ItemStack random(){
        Random random = new Random();
        int zufall = random.nextInt(crate.getItems().size());
        return crate.getItems().get(zufall);
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
