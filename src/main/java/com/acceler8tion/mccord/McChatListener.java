package com.acceler8tion.mccord;

import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

class McChatListener implements Listener {
    private static final Logger LOGGER = Bukkit.getLogger();
    private final ICorePlugin plugin;
    private TextChannel channel = null;

    McChatListener(@NotNull ICorePlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("ConstantConditions")
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if(channel == null) {
            channel = plugin.getJDA().getGuildById(CorePlugin.guildID)
                                    .getTextChannelById(CorePlugin.channelID);
        }
    }
}