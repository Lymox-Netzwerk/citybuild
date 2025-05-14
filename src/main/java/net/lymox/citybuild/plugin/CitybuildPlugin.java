package net.lymox.citybuild.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.commands.MünzenCommand;
import net.lymox.citybuild.commands.PayCommand;
import net.lymox.citybuild.commands.SetWarpCommand;
import net.lymox.citybuild.commands.WarpCommand;
import net.lymox.citybuild.listeners.PlayerJoinListener;
import net.lymox.citybuild.listeners.PlayerMoveListener;
import net.lymox.citybuild.listeners.PlayerQuitListener;
import net.lymox.citybuild.listeners.npc.shop.ShopPreventDeathListener;
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

        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("münzen").setExecutor(new MünzenCommand());
        getCommand("münzen").setTabCompleter(new MünzenCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warp").setTabCompleter(new WarpCommand());
        getCommand("pay").setExecutor(new PayCommand());
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
