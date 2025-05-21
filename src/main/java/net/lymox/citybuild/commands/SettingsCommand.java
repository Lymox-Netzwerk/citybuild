package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.ServerManager;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsCommand implements CommandExecutor, TabCompleter {

    ServerManager serverManager = CitybuildPlugin.getInstance().getManagers().getServerManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(!commandSender.hasPermission("lymox.citybuild.commands.settings")){
            commandSender.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
            return true;
        }

        if(args.length == 2){
            if(args[0].equalsIgnoreCase("maintenance")){
                if(args[1].equalsIgnoreCase("on")){
                    if(CitybuildPlugin.getInstance().getManagers().getServerManager().isMaintenance()){
                        commandSender.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Diese Einstellung wird schon angewendet!")));
                        return true;
                    }
                    CitybuildPlugin.getInstance().getManagers().getServerManager().setMaintenance(true);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if(!onlinePlayer.hasPermission(serverManager.getMaintenancePermission())){
                            onlinePlayer.kick(MiniMessage.miniMessage().deserialize("<red>CityBuild wird zurzeit gewartet."));
                        }
                    }
                    commandSender.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Wartungsmodus wurde <green>aktiviert<gray>.")));
                }
                if(args[1].equalsIgnoreCase("off")){
                    if(!CitybuildPlugin.getInstance().getManagers().getServerManager().isMaintenance()){
                        commandSender.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Diese Einstellung wird schon angewendet!")));
                        return true;
                    }
                    CitybuildPlugin.getInstance().getManagers().getServerManager().setMaintenance(false);
                    commandSender.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Wartungsmodus wurde <red>deaktiviert<gray>.")));
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player))return List.of();
        if(!player.hasPermission("lymox.citybuild.commands.settings"))return List.of();
        if(args.length == 1){
            List<String> tab = new ArrayList<>();
            tab.add("maintenance");
            return tab.stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if(args.length == 2){
            List<String> tab = new ArrayList<>();
            tab.add("on");
            tab.add("off");
            return tab.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
        }
        return List.of();
    }
}
