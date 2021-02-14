package com.acceler8tion.mccord.util;

import org.jetbrains.annotations.Nullable;

public final class PropertyChecker {
    private PropertyChecker() {
        //singleton
    }

    public static void valid(@Nullable String token, @Nullable String guildID, @Nullable String channelID) throws NoSuchFieldException {
        if(token == null || token.isEmpty()) throw new NoSuchFieldException("Empty field: `Token`");
        if(guildID == null || guildID.isEmpty()) throw new NoSuchFieldException("Empty field: `GuildID`");
        if(channelID == null || channelID.isEmpty()) throw new NoSuchFieldException("Empty field: `ChannelID`");
    }
}
