package net.lymox.citybuild.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.lymox.citybuild.manager.GUIManager;
import net.lymox.citybuild.manager.ShopManager;
import net.lymox.citybuild.manager.objects.crates.Crate;
import net.lymox.citybuild.manager.objects.shop.Categorie;
import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.ItemCreator;
import net.lymox.citybuild.utils.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShopCommand implements CommandExecutor, TabCompleter {

    public static Map<Player, ItemStack[]> items = new HashMap<>();

    ShopManager shopManager = CitybuildPlugin.getInstance().getManagers().getShopManager();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player)){
            commandSender.sendMessage(Messages.haveToBePlayer);
            return true;
        }

        if(!player.hasPermission("lymox.citybuild.commands.shop")){
            player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize(Messages.noPermission)));
            return true;
        }

        if(args.length == 0){
            sendHelp(player);
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("createCategorie")){
                String name = args[1];
                if(shopManager.getCategorie(name)!=null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Kategorie <b>" + name + "</b> gibt es schon!")));
                    return true;
                }
                shopManager.createCategorie(name);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Die Kategorie <b>" + name + "</b> wurde erstellt")));
            }
            if(args[0].equalsIgnoreCase("deleteCategorie")){
                String name = args[1];
                if(shopManager.getCategorie(name)==null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Kategorie <b>" + name + "</b> gibt es nicht!")));
                    return true;
                }
                shopManager.deleteCategorie(name);
                player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Die Kategorie <b>" + name + "</b> wurde gelöscht")));
            }else if(args[0].equalsIgnoreCase("list")){
                if(args[1].equalsIgnoreCase("categories")){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>Shop-Kategorien:")));
                    for (Categorie category : shopManager.getCategories()) {
                        player.sendMessage(category.getName() + " ["+(category.getMaterial()!=null?category.getMaterial().toString():"null")+"]");
                    }
                }
            }else if(args[0].equalsIgnoreCase("edit")){
                Categorie categorie = shopManager.getCategorie(args[1]);
                if(categorie==null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Kategorie <b>" + args[1] + "</b> gibt es nicht!")));
                    return true;
                }
                items.remove(player);
                items.put(player, player.getInventory().getContents());
                player.openInventory(new GUIManager().editCategorie(categorie));

            }
        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("material")){
                Categorie categorie = shopManager.getCategorie(args[1]);
                if(categorie==null){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Die Kategorie <b>" + args[1] + "</b> gibt es nicht!")));
                    return true;
                }
                try{
                    Material material = Material.valueOf(args[2]);
                    if(material==null){
                        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Das ist kein gültiges Material!")));
                        return true;
                    }
                    categorie.setMaterial(material);
                    categorie.save();
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<green>Material <b>" + material.toString() + "</b> für <i>" + categorie.getName() + "</i> wurde gesetzt!")));

                }catch (Exception e){
                    player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<red>Das ist kein gültiges Material!")));
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(commandSender instanceof Player player))return List.of();
        if(!player.hasPermission("lymox.citybuild.commands.shop"))return List.of();
        if(args.length == 1){
            ArrayList<String> list = new ArrayList<>();
            list.add("edit");
            list.add("createCategorie");
            list.add("deleteCategorie");
            list.add("material");
            list.add("list");
            return list.stream().filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("list")){
                ArrayList<String> list = new ArrayList<>();
                list.add("categories");
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("material")){
                ArrayList<String> list = new ArrayList<>();
                for (Categorie categorie : shopManager.getCategories()) {
                    list.add(categorie.getName());
                }
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("edit")){
                ArrayList<String> list = new ArrayList<>();
                for (Categorie categorie : shopManager.getCategories()) {
                    list.add(categorie.getName());
                }
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("deleteCategorie")){
                ArrayList<String> list = new ArrayList<>();
                for (Categorie categorie : shopManager.getCategories()) {
                    list.add(categorie.getName());
                }
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }
        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("list")){
                ArrayList<String> list = new ArrayList<>();
                for (Categorie categorie : shopManager.getCategories()) {
                    list.add(categorie.getName());
                }
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("material") && shopManager.getCategorie(args[1])!=null){
                ArrayList<String> list = new ArrayList<>();
                for (Material value : Material.values()) {
                    list.add(value.toString());
                }
                return list.stream().filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase())).collect(Collectors.toList());
            }
        }
        return List.of();
    }

    private void sendHelp(Player player){
        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<gray>/shop-Command Übersicht:")));
        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>/shop list categories")));
        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>/shop edit categorie <Categorie>")));
        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>/shop createCategorie <Categorie>")));
        player.sendMessage(CitybuildPlugin.getPrefix().append(MiniMessage.miniMessage().deserialize("<white>/shop material <Categorie> <Material>")));
    }
}
