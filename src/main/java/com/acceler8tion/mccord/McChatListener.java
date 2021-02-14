package com.acceler8tion.mccord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

class McChatListener implements Listener {
    private static final Logger LOGGER = Bukkit.getLogger();
    private final ICorePlugin plugin;

    McChatListener(@NotNull ICorePlugin plugin) {
        this.plugin = plugin;
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
    }
}