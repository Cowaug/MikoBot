package eBot;

import eBot.MediaPlayer.MediaPlayer;
import javafx.util.Pair;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.ArrayList;

public class MediaManager {
    private static ArrayList<Pair<Guild, MediaPlayer>> mediaManagerList = new ArrayList<>();

    /**
     * Try connecting to the voice channel
     * of a specific server
     * @param guild Server
     * @param voiceChannel Voice channel of that server
     * @return MediaPlayer of that server
     */
    public static MediaPlayer connectTo(Guild guild, VoiceChannel voiceChannel) {
        Pair<Guild, MediaPlayer> pair;

        if ((pair = findGuild(guild)) == null) {
            MediaPlayer mediaPlayer = new MediaPlayer(guild, voiceChannel);
            mediaManagerList.add(new Pair<>(guild, mediaPlayer));
            return mediaPlayer;
        } else {
            pair.getValue().reconnect(voiceChannel);
            return pair.getValue();
        }
    }

    /**
     * Find the MediaPlayer of specific server
     * @param guild Server
     * @return Pair of the server and it's MediaPlayer
     */
    private static Pair<Guild, MediaPlayer> findGuild(final Guild guild) {
        return mediaManagerList.stream().filter(p -> p.getKey().equals(guild)).findAny().orElse(null);
    }

    /**
     * Disconnect all MediaPlayer from it's server
     */
    public static void disconnectAll() {
        mediaManagerList.forEach(p -> p.getValue().disconnect());
    }
}
