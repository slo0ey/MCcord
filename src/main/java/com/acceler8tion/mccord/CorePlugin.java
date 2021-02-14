package com.acceler8tion.mccord;

import com.acceler8tion.mccord.util.PropertyChecker;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CorePlugin extends JavaPlugin implements ICorePlugin {
    private static final Logger LOGGER = Bukkit.getLogger();
    private JDA jda = null;
    public static String guildID;
    public static String channelID;

    @Override
    public void onEnable() {
        LOGGER.log(Level.INFO, ChatColor.GOLD + "Starting...");
        Bukkit.getPluginManager().registerEvents(new McChatListener(this), this);
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                try {
                        @NotNull
                        YamlConfiguration yaml;
                        String token;
                        try {
                                yaml = YamlConfiguration.loadConfiguration(new File("discord.yml"));
                                token = yaml.getString("Token");
                                guildID = yaml.getString("GuildID");
                                channelID = yaml.getString("ChannelID");

                                PropertyChecker.valid(token, guildID, channelID); //check if values are safe

                        } catch (IllegalArgumentException e) {
                                LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "`discord.yml` file does not exist!");
                                LOGGER.log(Level.WARNING, ChatColor.RED + "Check out `README.mccord.txt`");
                                return;
                        } catch (NoSuchFieldException e) {
                                LOGGER.log(Level.WARNING, ChatColor.DARK_RED + e.getMessage());
                                return;
                        }
                        LOGGER.log(Level.INFO, "Connect to " + ChatColor.BLUE + "Discord");
                        jda = JDABuilder.createLight(yaml.getString("token"))
                                        .addEventListeners(new DiscordChatListener())
                                        .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                                        .setActivity(Activity.playing("with Minecraft Players"))
                                        .setStatus(OnlineStatus.ONLINE)
                                        .build();
                } catch (IllegalArgumentException e) {
                        LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "You need to enable `Precense Intent` & `Server Members Intent`");
                        LOGGER.log(Level.WARNING, ChatColor.RED + "Visit here --> `https://discord.com/developers/applications`");
                } catch (LoginException e) {
                        LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "Failed to login!");
                        LOGGER.log(Level.WARNING, ChatColor.RED + "Try to use `/mccord reconnect` or check your token");
                }
        });
    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }

    @Override
    public JDA getJDA() {
        return jda;
    }
}
