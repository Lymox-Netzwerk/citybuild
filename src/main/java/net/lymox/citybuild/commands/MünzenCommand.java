package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.uuidfetcher.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MünzenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }

        if(args.length == 0){
            Userdata userdata = new Userdata(player.getUniqueId());
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Du bestitz <yellow>" + userdata.getMünzen() + " Münze"+(userdata.getMünzen()==1?"":"n")+"<gray>.")));
        }else if(args.length == 2) {
            if(!player.hasPermission("lymox.citybuild.commands.münzen")){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                return true;
            }
            if(args[0].equalsIgnoreCase("reset")){
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if(uuid == null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> existiert nicht")));
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if(!offlinePlayer.hasPlayedBefore()){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> war noch nicht auf dem Server!")));
                    return true;
                }
                Userdata userdata = new Userdata(uuid);
                userdata.setMünzen(0);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Die Münzenanzahl von <b>" + args[1] + "</b> wurde auf <i>" + userdata.getMünzen() + "</i> gesetzt!")));
                if(Bukkit.getPlayer(args[1])!=null){
                    CitybuildPlugin.getInstance().getManagers().getScoreboardManager().setDefaultScore(Bukkit.getPlayer(args[1]));
                }
            }else {
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if(uuid == null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> existiert nicht")));
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if(!offlinePlayer.hasPlayedBefore()){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> war noch nicht auf dem Server!")));
                    return true;
                }
                Userdata userdata = new Userdata(uuid);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray><b>"+args[1]+"</b> hat <yellow>" + userdata.getMünzen() + " Münzen<gray>.")));

            }
        }else if(args.length == 3){
            if(!player.hasPermission("lymox.citybuild.commands.münzen")){
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                return true;
            }
            if(args[0].equalsIgnoreCase("set")){
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if(uuid == null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> existiert nicht")));
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if(!offlinePlayer.hasPlayedBefore()){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> war noch nicht auf dem Server!")));
                    return true;
                }
                try {
                    Integer amount = Integer.parseInt(args[2]);
                    Userdata userdata = new Userdata(uuid);
                    userdata.setMünzen(amount);
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Die Münzenanzahl von <b>" + args[1] + "</b> wurde auf <i>" + userdata.getMünzen() + "</i> gesetzt!")));
                    if(Bukkit.getPlayer(args[1])!=null){
                        CitybuildPlugin.getInstance().getManagers().getScoreboardManager().setDefaultScore(Bukkit.getPlayer(args[1]));
                    }
                }catch (NumberFormatException e){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Anzahl muss eine Ziffer sein!")));
                }
            }else if(args[0].equalsIgnoreCase("add")){
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if(uuid == null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> existiert nicht")));
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if(!offlinePlayer.hasPlayedBefore()){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> war noch nicht auf dem Server!")));
                    return true;
                }
                try {
                    Integer amount = Integer.parseInt(args[2]);
                    Userdata userdata = new Userdata(uuid);
                    userdata.addMünzen(amount);
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Die Münzenanzahl von <b>" + args[1] + "</b> wurde um <i>" + amount + "</i> erhöht!")));
                    if(Bukkit.getPlayer(args[1])!=null){
                        CitybuildPlugin.getInstance().getManagers().getScoreboardManager().setDefaultScore(Bukkit.getPlayer(args[1]));
                    }
                }catch (NumberFormatException e){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Anzahl muss eine Ziffer sein!")));
                }
            }else if(args[0].equalsIgnoreCase("remove")){
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if(uuid == null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> existiert nicht")));
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if(!offlinePlayer.hasPlayedBefore()){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Der Spieler <dark_red>" + args[1] + "</dark_red><red> war noch nicht auf dem Server!")));
                    return true;
                }
                try {
                    Integer amount = Integer.parseInt(args[2]);
                    Userdata userdata = new Userdata(uuid);
                    userdata.removeMünzen(amount);
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Die Münzenanzahl von <b>" + args[1] + "</b> wurde um <i>" + amount + "</i> reduziert!")));
                    if(Bukkit.getPlayer(args[1])!=null){
                        CitybuildPlugin.getInstance().getManagers().getScoreboardManager().setDefaultScore(Bukkit.getPlayer(args[1]));
                    }
                }catch (NumberFormatException e){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Anzahl muss eine Ziffer sein!")));
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player))return List.of();
        if(args.length == 0){
            if(player.hasPermission("lymox.citybuild.commands.münzen")) {
                return List.of("set", "add", "remove", "reset");
            }
        }else if(args.length == 1){
            if(player.hasPermission("lymox.citybuild.commands.münzen")) {
                ArrayList<String> list = new ArrayList<>();
                list.add("set");
                list.add("add");
                list.add("remove");
                list.add("reset");
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            }
        }
        return List.of();
    }
}
