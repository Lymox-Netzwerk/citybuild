package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.LocationsManager;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class startend implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(!player.hasPermission("lymox.citybuild.commands.startend")){
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
            return true;
        }
        LocationsManager locationsManager = CitybuildPlugin.getInstance().getManagers().getLocationsManager();
        if(locationsManager.get("Warp.End")!=null){
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der End wurde schon aktiviert!")));
            return true;
        }
        Location location = new Location(Bukkit.getWorld("EndFarmwelt"), 120, 58, -12);
        locationsManager.add(location, "Warp.End");
        for (Entity entity : location.getWorld().getEntities()) {
            if(entity.getType().equals(EntityType.ENDER_DRAGON)){
                EnderDragon enderDragon = (EnderDragon) entity;
                enderDragon.setMaxHealth(1000);
                enderDragon.setHealth(1000);
            }
        }
        Bukkit.broadcastMessage("§k----------------------");
        Bukkit.broadcastMessage("§5§lDER END WURDE SOEBEN ERÖFFNET");
        Bukkit.broadcastMessage("§7ʜᴇʟғᴇ ᴅᴀʙᴇɪ ᴅᴇɴ ᴇɴᴅᴇʀᴅʀᴀᴄʜᴇɴ ᴢᴜ ᴛöᴛᴇɴ ᴜɴᴅ ᴇʀʜᴀʟᴛᴇ ᴇɪɴᴇ ʙᴇʟᴏʜɴᴜɴɢ");
        Bukkit.broadcastMessage("§7ᴠɪᴇʟʟᴇɪᴄʜᴛ sᴄʜᴀғғsᴛ ᴅᴜ ᴇs ᴊᴀ ᴅᴀs ᴅʀᴀᴄʜᴇɴᴇɪ ᴀᴜғᴢᴜsᴀᴍᴍᴇʟɴ");
        Bukkit.broadcastMessage("§5/warp End §fum zum End zu gelangen");
        Bukkit.broadcastMessage("§k----------------------");
        return true;
    }
}
