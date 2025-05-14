package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.LocationsManager;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SetWarpCommand implements CommandExecutor {

    private HashMap<Player, String> confim = new HashMap<>();
    private LocationsManager locationsManager = CitybuildPlugin.getInstance().getManagers().getLocationsManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(!player.hasPermission("lymox.citybuild.commands.setwarp")){
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
            return true;
        }
        if(args.length != 1){
            String message = Messages.wrongCommandUsage;
            message = message.replace("%c", "/setwarp <Warpname>");
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
        }else {
            if(args[0].equalsIgnoreCase("confirm")){
                if(!confim.containsKey(player)){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Es gibt nichts zum bestätigen!")));
                    return true;
                }
                String warpName = confim.get(player);
                locationsManager.addWarp(player.getLocation(), warpName);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Warp <b>"+warpName+"</b> wurde gesetzt.")));
                confim.remove(player);
                return true;
            }
            String warpName = args[0];
            if(locationsManager.get("Warp."+warpName)!=null){
                confim.remove(player);
                confim.put(player, warpName);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(confim.containsKey(player))confim.remove(player);
                    }
                }.runTaskLater(CitybuildPlugin.getInstance(), 20*10);

                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Ein Warp mit diesem Namen existiert schon!")));
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>/setWarp confirm, um die alte Location zu ersetzen. 10 Sekunden gültig")));
                return true;
            }
            locationsManager.addWarp(player.getLocation(), warpName);
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Warp <b>"+warpName+"</b> wurde gesetzt.")));
        }

        return true;
    }
}
