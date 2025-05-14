package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.LocationsManager;
import net.lymox.citybuild.manager.Managers;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private LocationsManager locationsManager = CitybuildPlugin.getInstance().getManagers().getLocationsManager();
    public static HashMap<Integer, Player> teleportReq = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(args.length != 1){
            String message = Messages.wrongCommandUsage;
            message = message.replace("%c", "/warp <Warpname>");
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(message)));
        }else {
            String warpName = args[0];
            if(locationsManager.get("Warp."+warpName)==null){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Diesen Warp gibt es nicht!")));
                return true;
            }

            if(player.hasPermission("lymox.citybuild.warp.instant")){
                Location location = locationsManager.get("Warp." + warpName);
                player.teleport(location);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Du wurdest zu <b>" + warpName + "</b> teleportiert.")));
                return true;
            }

            Integer integer = new Random().nextInt(1000);

            if(!teleportReq.containsValue(player)) {
                teleportReq.put(integer, player);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Du wirst in 3 Sekunden teleportiert.")));
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>Bewege dich nicht")));
            }else {
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Du wirst schon teleportiert!")));
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(teleportReq.containsKey(integer)) {
                        Location location = locationsManager.get("Warp." + warpName);
                        player.teleport(location);
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Du wurdest zu <b>" + warpName + "</b> teleportiert.")));
                        teleportReq.remove(integer);
                    }
                }
            }.runTaskLater(CitybuildPlugin.getInstance(), 20*3);


        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            return CitybuildPlugin.getInstance().getManagers().getLocationsManager().getWarps();
        }else if(args.length == 1){
            return CitybuildPlugin.getInstance().getManagers().getLocationsManager().getWarps().stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }
}
