package net.lymox.citybuild.manager;

import net.lymox.citybuild.plugin.CitybuildPlugin;
import net.lymox.citybuild.utils.Userdata;
import net.lymox.core.master.objects.clan.ClanMember;
import net.lymox.core.master.objects.player.LymoxPlayer;
import net.lymox.core.master.service.LymoxPlayerService;
import net.lymox.core.master.service.clan.ClanMemberService;
import net.lymox.core.spigot.builder.scoreboard.ScoreboardBuilder;
import net.lymox.core.spigot.builder.tablist.TablistBuilder;
import net.lymox.core.spigot.plugin.CoreSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManger {

    private final LymoxPlayerService playerService;
    private final ClanMemberService clanMemberService;

    public ScoreboardManger() {
        this.playerService = CoreSpigotPlugin.getInstance().getPlayerService();
        this.clanMemberService = CoreSpigotPlugin.getInstance().getClanMemberService();
    }

    public void setDefaultScore(Player player) {
        updateBoard(player);
        startUpdatingBoard(player);
    }

    private void updateBoard(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        ClanMember clanMember = clanMemberService.loadSettings(player.getUniqueId());
        LymoxPlayer lymoxPlayer = playerService.loadPlayer(player.getUniqueId());

        final ScoreboardBuilder sidebarBuilder = new ScoreboardBuilder("\ue073");

        sidebarBuilder.addLine("     ", 12);
        sidebarBuilder.addLine("&8» &7&lᴅᴇɪɴ ʀᴀɴɢ", 11);
        sidebarBuilder.addLine(" &8│ &a&l" + lymoxPlayer.displayName(), 10);
        sidebarBuilder.addLine(" ", 9);
        sidebarBuilder.addLine("&8» &7&lᴅᴇɪɴᴇ ᴍüɴᴢᴇɴ", 8);
        sidebarBuilder.addLine(" &8│ &e&l" + new Userdata(player.getUniqueId()).getMünzen() + " \ue03c", 7);
        sidebarBuilder.addLine("  ", 6);
        sidebarBuilder.addLine("&8» &7&lᴅᴇɪɴ ᴄʟᴀɴ", 5);

        if (clanMember.getClan() == null) {
            sidebarBuilder.addLine(" &8│ &c&lᴋᴇɪɴ ᴄʟᴀɴ", 4);
        } else {
            sidebarBuilder.addLine(" &8│ " + getFormatedClanColor(clanMember.getClan().getActiveColor()) + clanMember.getClan().getClanTag().toUpperCase(), 4);
        }

        sidebarBuilder.addLine("   ", 3);
        sidebarBuilder.addLine("&8» &7&lᴅᴇɪɴᴇ sᴘɪᴇʟᴢᴇɪᴛ", 2);
        sidebarBuilder.addLine(" &8│ &a&l" + FormatedPlaytime(player), 1);
        sidebarBuilder.addLine("    ", 0);
        if(CitybuildPlugin.getInstance().getManagers().getServerManager().isMaintenance()){
            sidebarBuilder.addLine("§cᴡᴀʀᴛᴜɴɢᴇɴ sɪɴᴅ ᴀᴋᴛɪᴠ", -1);
        }

        sidebarBuilder.setToPlayer(player);

        TablistBuilder tablistBuilder = new TablistBuilder();
        Bukkit.getOnlinePlayers().forEach(tablistBuilder::setDefaultTablist);

    }

        private void startUpdatingBoard(Player player) {
        Bukkit.getScheduler().runTaskTimer(CitybuildPlugin.getInstance(), () -> updateBoard(player), 60L, 60L); // Alle 60 Sekunden (1200 Ticks)
    }

        public void deleteBoard(Player player) {
        final ScoreboardBuilder sidebarBuilder = new ScoreboardBuilder("\ue06b");

        sidebarBuilder.delete(player);
    }

        private String getFormatedClanColor(String value) {
        return switch (value.toLowerCase()) {
            case "black" -> "§0";
            case "black_special" -> "§0§l";
            case "dark_blue" -> "§1";
            case "dark_blue_special" -> "§1§l";
            case "dark_green" -> "§2";
            case "dark_green_special" -> "§2§l";
            case "dark_aqua" -> "§3";
            case "dark_aqua_special" -> "§3§l";
            case "dark_red" -> "§4";
            case "dark_red_special" -> "§4§l";
            case "dark_purple" -> "§5";
            case "dark_purple_special" -> "§5§l";
            case "gold" -> "§6";
            case "gold_special" -> "§6§l";
            case "gray" -> "§7";
            case "gray_special" -> "§7§l";
            case "dark_gray" -> "§8";
            case "dark_gray_special" -> "§8§l";
            case "blue" -> "§9";
            case "blue_special" -> "§9§l";
            case "green" -> "§a";
            case "green_special" -> "§a§l";
            case "aqua" -> "§b";
            case "aqua_special" -> "§b§l";
            case "red" -> "§c";
            case "red_special" -> "§c§l";
            case "light_purple" -> "§d";
            case "light_purple_special" -> "§d§l";
            case "yellow" -> "§e";
            case "yellow_special" -> "§e§l";
            case "white" -> "§f";
            case "white_special" -> "§f§l";
            default -> "§f";
        };
    }

        private String FormatedPlaytime(Player player) {
        LymoxPlayer lymoxPlayer = playerService.loadPlayer(player.getUniqueId());
        long totalSeconds = lymoxPlayer.getPlaytime();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;

        return hours > 0 ? "§a§l" + hours + " §7" + (hours == 1 ? "sᴛᴜɴᴅᴇ" : "sᴛᴜɴᴅᴇɴ") : "§b§l" + minutes + " §7" + (minutes == 1 ? "ᴍɪɴᴜᴛᴇ" : "ᴍɪɴᴜᴛᴇɴ");
    }


}
