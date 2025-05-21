package net.lymox.citybuild.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.commands.*;
import net.lymox.citybuild.listeners.PlayerJoinListener;
import net.lymox.citybuild.listeners.PlayerMoveListener;
import net.lymox.citybuild.listeners.PlayerQuitListener;
import net.lymox.citybuild.listeners.crates.CratesClickListener;
import net.lymox.citybuild.listeners.crates.CratesMenuListener;
import net.lymox.citybuild.listeners.crates.CratesInventoryListener;
import net.lymox.citybuild.listeners.npc.shop.ShopPreventDeathListener;
import net.lymox.citybuild.listeners.shop.ShopClickListener;
import net.lymox.citybuild.listeners.shop.ShopEditClickListener;
import net.lymox.citybuild.manager.Managers;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CitybuildPlugin extends JavaPlugin {

    private static CitybuildPlugin instance;
    private Managers managers;

    @Override
    public void onEnable() {
        instance = this;
        managers = new Managers();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopPreventDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new CratesInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new CratesMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new CratesClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopEditClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopClickListener(), this);

        getCommand("setwarp").setExecutor(new SetWarpCommand());
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
}
