package net.lymox.citybuild.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class CitybuildPlugin extends JavaPlugin {

    private static CitybuildPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
    }

    public static Component getPrefix() {
        return MiniMessage.miniMessage().deserialize("<dark_gray>»</dark_gray> <gradient:#55ffff:#00aaaa><b>Cores</b> <dark_gray>〢 <gray>");
    }
}
