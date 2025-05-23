package net.lymox.citybuild.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.commands.*;
import net.lymox.citybuild.listeners.*;
import net.lymox.citybuild.listeners.crates.CratesClickListener;
import net.lymox.citybuild.listeners.crates.CratesMenuListener;
import net.lymox.citybuild.listeners.crates.CratesInventoryListener;
import net.lymox.citybuild.listeners.elytra.ElytraListener;
import net.lymox.citybuild.listeners.entitys.EntityDeathListener;
import net.lymox.citybuild.listeners.npc.shop.ShopPreventDeathListener;
import net.lymox.citybuild.listeners.shop.ShopClickListener;
import net.lymox.citybuild.listeners.shop.ShopEditClickListener;
import net.lymox.citybuild.listeners.storage.StorageClickListener;
import net.lymox.citybuild.listeners.world.WorldBreakListener;
import net.lymox.citybuild.listeners.world.WorldMobSpawnListener;
import net.lymox.citybuild.manager.Managers;
import net.lymox.citybuild.utils.ClearLag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.chunky.api.ChunkyAPI;

public class CitybuildPlugin extends JavaPlugin {

    private static CitybuildPlugin instance;
    private Managers managers;
    private ClearLag clearLag;
    private ChunkyAPI chunky;

    @Override
    public void onEnable() {
        instance = this;
        managers = new Managers();
        clearLag = new ClearLag();
        chunky = Bukkit.getServer().getServicesManager().load(ChunkyAPI.class);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleportMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopPreventDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new CratesInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new CratesMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new CratesClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopEditClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldMobSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new StorageClickListener(), this);

        new ElytraListener();

        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("delwarp").setExecutor(new DelWarpCommand());
        getCommand("delwarp").setTabCompleter(new DelWarpCommand());
        getCommand("münzen").setExecutor(new MünzenCommand());
        getCommand("münzen").setTabCompleter(new MünzenCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warp").setTabCompleter(new WarpCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("crates").setExecutor(new CratesCommand());
        getCommand("crates").setTabCompleter(new CratesCommand());
        getCommand("shop").setExecutor(new ShopCommand());
        getCommand("shop").setTabCompleter(new ShopCommand());
        getCommand("item").setExecutor(new ItemCommand());
        getCommand("item").setTabCompleter(new ItemCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("settings").setTabCompleter(new SettingsCommand());
        getCommand("storage").setExecutor(new StorageCommand());

//        Location farmwelt = getManagers().getLocationsManager().get("Warp.Farmwelt");
//        if(farmwelt != null){
//            if(chunky.version()==0){
//                chunky.startTask(farmwelt.getWorld().getName(), "square", farmwelt.getBlockX(), farmwelt.getBlockZ(), 5000, 5000, "concentric");
//            }
//        }
    }

    @Override
    public void onDisable() {
    }

    public static CitybuildPlugin getInstance() {
        return instance;
    }

    public static Component getPrefix() {
        return MiniMessage.miniMessage().deserialize("<dark_gray>»</dark_gray> <gradient:#55ff55:#00aa00><b>CityBuild</b> <dark_gray>〢 <gray>");
    }

    public Managers getManagers() {
        return managers;
    }

    public ChunkyAPI getChunky() {
        return chunky;
    }
}
