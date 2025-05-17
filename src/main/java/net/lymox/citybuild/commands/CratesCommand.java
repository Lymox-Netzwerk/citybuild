package net.lymox.citybuild.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.CratesManager;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.objects.Crate;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Messages;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.citybuild.uuidfetcher.UUIDFetcher;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CratesCommand implements CommandExecutor, TabCompleter {

    private CratesManager cratesManager = CitybuildPlugin.getInstance().getManagers().getCratesManager();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }
        if(args.length == 0){

        }else {
            if(args.length == 1){
                if(!player.hasPermission("lymox.citybuild.commands.crates")){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                    return true;
                }
                if(args[0].equalsIgnoreCase("list")){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Crates-Liste:")));
                    for (Crate crate : CitybuildPlugin.getInstance().getManagers().getCratesManager().getCrates()) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<white>- " + crate.getName() + " <gray><click:run_command:'/crates edit " + crate.getName() + "'>[bearbeiten]</click>"));
                    }
                }
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("create")){
                    if(!player.hasPermission("lymox.citybuild.commands.crates")){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                        return true;
                    }
                    String name = args[1];
                    if(cratesManager.getCrate(name)!=null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Ein Crate mit diesem Namen existiert schon!")));
                        return true;
                    }
                    cratesManager.createCrate(name);
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Crate <b>" + name + "</b> wurde erstellt!")));
                }else if(args[0].equalsIgnoreCase("delete")){
                    if(!player.hasPermission("lymox.citybuild.commands.crates")){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                        return true;
                    }
                    String name = args[1];
                    Crate crate = cratesManager.getCrate(name);
                    if(crate==null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Ein Crate mit diesem Namen gibt es nicht!")));
                        return true;
                    }
                    cratesManager.deleteCrate(crate.getName());
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Crate <b>" + name + "</b> wurde gelöscht!")));
                }else if(args[0].equalsIgnoreCase("edit")){
                    if(!player.hasPermission("lymox.citybuild.commands.crates")){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                        return true;
                    }
                    String name = args[1];
                    Crate crate = cratesManager.getCrate(name);
                    if(crate==null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Ein Crate mit diesem Namen nicht!")));
                        return true;
                    }
                    player.openInventory(new GUIManager().editCrate(crate));
                }else if(args[0].equalsIgnoreCase("info")){
                    if(!player.hasPermission("lymox.citybuild.commands.crates.give") && !player.hasPermission("lymox.citybuild.commands.crates")){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                        return true;
                    }
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
                    if(userdata.getCrates().isEmpty()){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(player.getUniqueId()).displayColor()+offlinePlayer.getName() + " <gray>hat keine Crates")));
                        return true;
                    }
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(CoreSpigotPlugin.getInstance().getPlayerService().loadPlayer(player.getUniqueId()).displayColor()+offlinePlayer.getName() + " <gray>hat diese Crates:")));
                    for (net.lymox.citybuild.utils.userdata.Crate crateH : userdata.getCrates()) {
                        Crate crate = cratesManager.getCrate(crateH.getId());
                        if(crate!=null){
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<white>" + crate.getName() + " <gray>➤ <white>" + crateH.getAmount()));
                        }
                    }
                }
            }else if(args.length == 4){
                if(!player.hasPermission("lymox.citybuild.commands.crates.give") && !player.hasPermission("lymox.citybuild.commands.crates")){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
                    return true;
                }
                if(args[0].equalsIgnoreCase("give")){
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
                    String crateName = args[2];
                    Crate crate = cratesManager.getCrate(crateName);
                    if(crate==null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Ein Crate mit diesem Namen gibt es nicht!")));
                        return true;
                    }
                    try {
                        int amount = Integer.parseInt(args[3]);
                        userdata.addCrate(crate.getId(), amount);
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray><b>" + offlinePlayer.getName() + "</b> wurden <i>" + amount + " " + crate.getName() + "</i>-Crate"+(amount==1?"":"s")+" hinzugefügt")));
                    }catch (NumberFormatException e){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Anzahl muss eine Ziffer sein!")));
                        e.printStackTrace();
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
                    Userdata userdata = new Userdata(uuid);
                    String crateName = args[2];
                    Crate crate = cratesManager.getCrate(crateName);
                    if(crate==null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Ein Crate mit diesem Namen gibt es nicht!")));
                        return true;
                    }
                    try {
                        int amount = Integer.parseInt(args[3]);
                        userdata.removeCrate(crate.getId(), amount);
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray><b>" + offlinePlayer.getName() + "</b> wurden <i>" + amount + " " + crate.getName() + "</i>-Crate"+(amount==1?"":"s")+" entfernt")));
                    }catch (NumberFormatException e){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Anzahl muss eine Ziffer sein!")));
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player))return List.of();
        if(args.length == 1){
            ArrayList<String> list = new ArrayList<>();
            if(player.hasPermission("lymox.citybuild.commands.crates")){
                list.add("create");
                list.add("delete");
                list.add("list");
            }
            if(player.hasPermission("lymox.citybuild.commands.crates.give")){
                list.add("info");
                list.add("give");
                list.add("remove");
            }
            return list.stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("info")){
                if(player.hasPermission("lymox.citybuild.commands.crates.give")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        list.add(onlinePlayer.getName());
                    }
                    return list.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                }
            }
            if(args[0].equalsIgnoreCase("delete")){
                if(player.hasPermission("lymox.citybuild.commands.crates")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Crate crate : cratesManager.getCrates()) {
                        list.add(crate.getName());
                    }
                    return list.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                }
            }
        }else if(args.length == 3){
            if(player.hasPermission("lymox.citybuild.commands.crates")) {
                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Crate crate : cratesManager.getCrates()) {
                        list.add(crate.getName());
                    }
                    return list.stream().filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());
                }
            }
        }

        return List.of();
    }
}
