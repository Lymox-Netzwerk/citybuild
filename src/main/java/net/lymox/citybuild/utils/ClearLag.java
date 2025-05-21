package net.lymox.citybuild.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class ClearLag {

    private int time;

    public ClearLag() {
        time = 20*60*10;
        forceClearLag();
        start();
    }

    public void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CitybuildPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(time == 20*60*5){
                    String message = Messages.clearlag;
                    message = message.replace("%c", "5");
                    Bukkit.broadcast(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
                }
                if(time == 20*60){
                    String message = Messages.clearlag;
                    message = message.replace("%c", "1");
                    Bukkit.broadcast(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
                }
                if(time==0){
                    int removed = 0;
                    for (World world : Bukkit.getWorlds()) {
                        for (Entity entity : world.getEntities()) {
                            if(entity instanceof Item){
                                removed++;
                                entity.remove();
                            }
                        }
                    }
                    Bukkit.broadcast(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Es wurden <b>" + removed + " Gegenstände</b> entfernt")));
                    time=20*60*10;
                }
                time--;
            }
        }, 0, 20);
    }

    public void forceClearLag(){
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if(entity instanceof Item){
                    entity.remove();
                }
            }
        }
    }

}
