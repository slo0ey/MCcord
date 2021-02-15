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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CorePlugin extends JavaPlugin implements ICorePlugin {
    private static final Logger LOGGER = Bukkit.getLogger();
    private static final AtomicBoolean ready = new AtomicBoolean(false);
    public static String guildID;
    public static String channelID;
    public Integer serverPresenceUpdateTaskId = null;
    private JDA jda = null;

    @Override
    public void onEnable() {
        LOGGER.log(Level.INFO, ChatColor.GOLD + "Starting...");
        Bukkit.getPluginManager().registerEvents(new McEventListener(this), this);

        onConnect();
    }

    @Override
    public void onConnect() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                @NotNull
                YamlConfiguration yaml;
                String token;
                try {
                    yaml = YamlConfiguration.loadConfiguration(new File("mccord.yml"));
                    token = yaml.getString("Token");
                    guildID = yaml.getString("GuildID");
                    channelID = yaml.getString("ChannelID");

                    PropertyChecker.valid(token, guildID, channelID); //check if values are safe

                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "`mccord.yml` file does not exist!");
                    LOGGER.log(Level.WARNING, ChatColor.RED + "Read this `https://github.com/acceler8tion/MCcord/tree/master#how-to-use`");
                    return;
                } catch (NoSuchFieldException e) {
                    LOGGER.log(Level.WARNING, ChatColor.DARK_RED + e.getMessage());
                    return;
                }
                LOGGER.log(Level.INFO, "Connect to " + ChatColor.BLUE + "Discord");
                jda = JDABuilder.createLight(token)
                        .addEventListeners(new DiscordEventListener(this))
                        .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                        .setActivity(Activity.playing("with Minecraft Players"))
                        .setStatus(OnlineStatus.ONLINE)
                        .build().awaitReady();
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "You need to enable `Presence Intent` & `Server Members Intent`");
                LOGGER.log(Level.WARNING, ChatColor.RED + "Visit here --> `https://discord.com/developers/applications`");
            } catch (LoginException e) {
                LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "Failed to login!");
                LOGGER.log(Level.WARNING, ChatColor.RED + "Try to use `/mccord reconnect` or check your token if it is valid");
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, ChatColor.DARK_RED + "Thread interrupted! RESTART SERVER");
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onDisable() {
        if(serverPresenceUpdateTaskId != null) {
            Bukkit.getScheduler().cancelTask(serverPresenceUpdateTaskId);
            serverPresenceUpdateTaskId = null;
        }
        jda.getGuildById(guildID).getTextChannelById(channelID).getManager().setTopic(String.format("Server: OFF, Online Players(0/%d)", Bukkit.getMaxPlayers())).queue();
        jda.shutdownNow();
    }

    @Override
    public void onLoop(Runnable runnable) {
        serverPresenceUpdateTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, runnable, 0, 200);
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    static void ready() {
        ready.set(true);
    }

    static boolean isReady() {
        return ready.get();
    }
}
