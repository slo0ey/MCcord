package com.acceler8tion.mccord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.acceler8tion.mccord.CorePlugin.channelID;
import static com.acceler8tion.mccord.CorePlugin.guildID;

class DiscordEventListener extends ListenerAdapter {
    private static final Logger LOGGER = Bukkit.getLogger();
    private final ICorePlugin plugin;

    DiscordEventListener(ICorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        JDA jda = event.getJDA();
        Guild guild = jda.getGuildById(guildID);
        if(guild != null) {
            if(guild.getTextChannelById(channelID) == null) {
                LOGGER.log(Level.WARNING, "TextChannel <" + channelID + "> does not exist");
                jda.shutdown();
                return;
            } else {
                TextChannel channel = guild.getTextChannelById(channelID);
                assert channel != null;
                if(PermissionUtil.checkPermission(channel, guild.getSelfMember(), Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL)) {
                    if(!PermissionUtil.checkPermission(guild.getSelfMember(), Permission.MANAGE_CHANNEL)) {
                        LOGGER.log(Level.INFO, "Updating server presence at Channel's Topic: " + ChatColor.DARK_RED + "OFF");
                        LOGGER.log(Level.INFO, "This requires " + ChatColor.BLUE + "Permission.MANAGE_CHANNEL");
                    } else {
                        LOGGER.log(Level.INFO, "Updating server presence : " + ChatColor.GREEN + "ON");
                        plugin.onLoop(() -> {
                                channel.getManager().setTopic(String.format("Server: ON, Online Players(%d/%d)", Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers())).queue();
                        });
                    }
                } else {
                    LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "MESSAGE_READ, MESSAGE_WRITE, and VIEW_CHANNEL permissions are required.");
                }
            }
        } else {
            LOGGER.log(Level.WARNING, "Guild <" + guildID + "> does not exist");
            jda.shutdown();
            return;
        }

        CorePlugin.ready();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(CorePlugin.isReady()) {
            try {
                if (event.getJDA().getSelfUser().getIdLong() != event.getAuthor().getIdLong() && event.getGuild().getId().equals(CorePlugin.guildID) && event.getChannel().getId().equals(CorePlugin.channelID)) {
                    final Message message = event.getMessage();
                    final User user = event.getAuthor();
                    String content = message.getContentRaw();
                    for (final User u : message.getMentionedUsers()) {
                        content = content.replace("<@" + u.getId() + ">", ChatColor.BLUE + "@" + u.getName() + ChatColor.RESET);
                    }
                    for (final Member m : message.getMentionedMembers()) {
                        content = content.replace("<@!" + m.getId() + ">", ChatColor.BLUE + "@" + m.getUser().getName() + ChatColor.RESET);
                    }
                    for (final TextChannel c : message.getMentionedChannels()) {
                        content = content.replace(c.getAsMention(), ChatColor.BLUE + "#" + c.getName() + ChatColor.RESET);
                    }
                    for (final Role r : message.getMentionedRoles()) {
                        content = content.replace(r.getAsMention(), (r.getColor() == null ?
                                net.md_5.bungee.api.ChatColor.WHITE : net.md_5.bungee.api.ChatColor.of(r.getColor())) + "@" + r.getName() + ChatColor.RESET);
                    }
                    for (final Emote e : message.getEmotes()) {
                        content = content.replace(e.getAsMention(), ":" + e.getName() + ":");
                    }
                    Bukkit.broadcastMessage(
                            String.format("%s%sDiscord %s<%s> %s", ChatColor.AQUA, ChatColor.BOLD, ChatColor.RESET, user.getAsTag(), content)
                    );
                }
            } catch (PermissionException e) {
                LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "MESSAGE_READ, MESSAGE_WRITE, and VIEW_CHANNEL permissions are required.");
            }
        }
    }
}
