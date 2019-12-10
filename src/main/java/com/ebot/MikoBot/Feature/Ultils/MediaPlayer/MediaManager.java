package com.ebot.MikoBot.Feature.Ultils.MediaPlayer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.ArrayList;

public class MediaManager {
    private static ArrayList<GuildMediaInstance> mediaManagerList = new ArrayList<>();

    /**
     * Try connecting to the voice channel
     * of a specific server
     * <p>
     * Manage list of media instance
     *
     * @param guild        Server
     * @param voiceChannel Voice channel of that server
     * @return MediaPlayer of that server
     */
    public static MediaInstance connectTo(Guild guild, VoiceChannel voiceChannel) {
        if (voiceChannel == null) return null;
        GuildMediaInstance guildMediaInstance;

        if ((guildMediaInstance = findGuild(guild)) == null) {
            MediaInstance mediaInstance = new MediaInstance(guild, voiceChannel);
            mediaManagerList.add(new GuildMediaInstance(guild, mediaInstance));
            return mediaInstance;
        } else {
            guildMediaInstance.getMediaInstance().reconnect(voiceChannel);
            return guildMediaInstance.getMediaInstance();
        }
    }

    /**
     * Find the MediaPlayer of specific server
     *
     * @param guild Server
     * @return Pair of the server and it's MediaPlayer
     */
    private static GuildMediaInstance findGuild(final Guild guild) {
        return mediaManagerList.stream().filter(p -> p.getGuild().equals(guild)).findAny().orElse(null);
    }

    /**
     * Disconnect all MediaPlayer from it's server
     */
    public static void disconnectAll() {
        mediaManagerList.forEach(p -> p.getMediaInstance().disconnect());
    }
}


class GuildMediaInstance {
    private Guild guild;
    private MediaInstance mediaInstance;

    GuildMediaInstance(Guild guild, MediaInstance mediaInstance) {
        this.guild = guild;
        this.mediaInstance = mediaInstance;
    }

    Guild getGuild() {
        return guild;
    }

    MediaInstance getMediaInstance() {
        return mediaInstance;
    }
}
