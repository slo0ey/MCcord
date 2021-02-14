package com.acceler8tion.mccord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

class DiscordChatListener extends ListenerAdapter {
    private static final Logger LOGGER = Bukkit.getLogger();
    private final ICorePlugin plugin;

    DiscordChatListener(@NotNull ICorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final Message message = event.getMessage();
        final User user = event.getAuthor();
    }
}
