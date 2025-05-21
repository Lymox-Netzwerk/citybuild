package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.LocationsManager;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DelWarpCommand implements CommandExecutor, TabCompleter {

    private HashMap<Player, String> confim = new HashMap<>();
    private LocationsManager locationsManager = CitybuildPlugin.getInstance().getManagers().getLocationsManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(!player.hasPermission("lymox.citybuild.commands.delwarp")){
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
            return true;
        }
        if(args.length != 1){
            String message = Messages.wrongCommandUsage;
            message = message.replace("%c", "/delwarp <Warpname>");
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
        }else {
            if(args[0].equalsIgnoreCase("confirm")){
                if(!confim.containsKey(player)){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Es gibt nichts zum bestätigen!")));
                    return true;
                }
                String warpName = confim.get(player);
                locationsManager.remove("Warp."+warpName);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Warp <b>"+warpName+"</b> wurde gelöscht.")));
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

                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>/delWarm confirm, um den Warp <b>entgültig</b> zu löschen. 10 Sekunden gültig")));
                return true;
            }else {
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Diesen Warp gibt es nicht!")));
            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!commandSender.hasPermission("lymox.citybuild.commands.delwarp"))return List.of();
        if(args.length == 0){
            return CitybuildPlugin.getInstance().getManagers().getLocationsManager().getWarps();
        }else if(args.length == 1){
            return CitybuildPlugin.getInstance().getManagers().getLocationsManager().getWarps().stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }

}
