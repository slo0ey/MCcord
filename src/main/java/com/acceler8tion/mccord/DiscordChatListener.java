package com.acceler8tion.mccord;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

class DiscordChatListener extends ListenerAdapter {
    private static final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final Message message = event.getMessage();
        final User user = event.getAuthor();
        String content = message.getContentRaw();
        for(final Member m : message.getMentionedMembers()) {
            content = content.replace(m.getAsMention(), ChatColor.BLUE + m.getUser().getAsMention());
        }
        for(final TextChannel c : message.getMentionedChannels()) {
            content = content.replace(c.getAsMention(), ChatColor.BLUE + c.getAsMention());
        }
        for(final Role r : message.getMentionedRoles()) {
            final net.md_5.bungee.api.ChatColor color = r.getColor() == null ?
                    net.md_5.bungee.api.ChatColor.WHITE : net.md_5.bungee.api.ChatColor.of(r.getColor());
            content = content.replace(r.getAsMention(), color + r.getAsMention());
        }
        Bukkit.broadcastMessage(
                String.format("%s%sDiscord %s%s> %s", ChatColor.AQUA, ChatColor.BOLD, ChatColor.RESET, user.getAsTag(), content)
        );
    }
}
