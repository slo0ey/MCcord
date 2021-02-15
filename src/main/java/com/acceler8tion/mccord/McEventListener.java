package com.acceler8tion.mccord;

import net.dv8tion.jda.api.entities.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;
import java.util.regex.Pattern;

@SuppressWarnings("ConstantConditions")
class McEventListener implements Listener {
    private static final Logger LOGGER = Bukkit.getLogger();
    private static final Pattern USER_AND_ROLE_PATTERN = Pattern.compile("@([a-zA-Zㄱ-ㅎ가-힣]+)");
    private static final Pattern CHANNEL_PATTERN = Pattern.compile("#([a-zA-Zㄱ-ㅎ가-힣-]+)");
    private static final Pattern EMOTE_PATTERN = Pattern.compile(":([a-zA-Zㄱ-ㅎ가-힣-_]+):");
    private final ICorePlugin plugin;

    McEventListener(@NotNull ICorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(CorePlugin.isReady()){
            final Player player = event.getPlayer();
            final Guild guild = plugin.getJDA().getGuildById(CorePlugin.guildID);
            String content = event.getMessage();

            //TODO: Next feature?

            /*Matcher userAndRoleMatches, channelMatches, emoteMatches;
            userAndRoleMatches = USER_AND_ROLE_PATTERN.matcher(content);
            while (userAndRoleMatches.find()) {
                String group = userAndRoleMatches.group().substring(1);
                LOGGER.log(Level.INFO, group);
                List<User> us = plugin.getJDA().getUsersByName(group, true);
                if(us.size() > 0) {
                    User u = us.get(0);
                    content = content.replace("@" + group, u.getAsMention());
                }
                List<Member> ms = guild.getMembersByName(group, true);
                if (ms.size() > 0) {
                    Member m = ms.get(0);
                    content = content.replace("@" + group, m.getAsMention());
                }
                List<Role> rs = guild.getRolesByName(group, true);
                if (rs.size() > 0) {
                    Role r = rs.get(0);
                    content = content.replace("@" + group, r.getAsMention());
                }
            }
            channelMatches = CHANNEL_PATTERN.matcher(content);
            while (channelMatches.find()) {
                String group = channelMatches.group().substring(1);
                List<TextChannel> cs = guild.getTextChannelsByName(group, true);
                if (cs.size() > 0) {
                    TextChannel c = cs.get(0);
                    content = content.replace("#" + group, c.getAsMention());
                }
            }
            emoteMatches = EMOTE_PATTERN.matcher(content);
            while (emoteMatches.find()) {
                String group = emoteMatches.group();
                group = group.substring(1, group.length() - 1);
                List<Emote> es = guild.getEmotesByName(group, true);
                if (es.size() > 0) {
                    Emote e = es.get(0);
                    content = content.replace(":" + group + ":", e.getAsMention());
                }
            }*/
            guild.getTextChannelById(CorePlugin.channelID)
                    .sendMessage(String.format("**%s >>** %s", player.getDisplayName(), content)).queue();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(CorePlugin.isReady()) {
            plugin.getJDA().getGuildById(CorePlugin.guildID)
                            .getTextChannelById(CorePlugin.channelID)
                            .sendMessage(String.format(":white_check_mark: **%s** joined.", event.getPlayer().getDisplayName())).queue();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(CorePlugin.isReady()) {
            plugin.getJDA().getGuildById(CorePlugin.guildID)
                    .getTextChannelById(CorePlugin.channelID)
                    .sendMessage(String.format(":no_entry: **%s** left.", event.getPlayer().getDisplayName())).queue();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(CorePlugin.isReady()) {
            plugin.getJDA().getGuildById(CorePlugin.guildID)
                    .getTextChannelById(CorePlugin.channelID)
                    .sendMessage(event.getDeathMessage()).queue();
        }
    }
}