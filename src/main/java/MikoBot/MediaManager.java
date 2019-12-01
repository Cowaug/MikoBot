package MikoBot;

import MikoBot.MediaPlayer.MediaInstance;
import javafx.util.Pair;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.ArrayList;

public class MediaManager {
    private static ArrayList<Pair<Guild, MediaInstance>> mediaManagerList = new ArrayList<>();

    /**
     * Try connecting to the voice channel
     * of a specific server
     *
     * Manage list of media instance
     *
     * @param guild Server
     * @param voiceChannel Voice channel of that server
     * @return MediaPlayer of that server
     */
    public static MediaInstance connectTo(Guild guild, VoiceChannel voiceChannel) {
        if(voiceChannel == null) return null;
        Pair<Guild, MediaInstance> pair;

        if ((pair = findGuild(guild)) == null) {
            MediaInstance mediaInstance = new MediaInstance(guild, voiceChannel);
            mediaManagerList.add(new Pair<>(guild, mediaInstance));
            return mediaInstance;
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
    private static Pair<Guild, MediaInstance> findGuild(final Guild guild) {
        return mediaManagerList.stream().filter(p -> p.getKey().equals(guild)).findAny().orElse(null);
    }

    /**
     * Disconnect all MediaPlayer from it's server
     */
    public static void disconnectAll() {
        mediaManagerList.forEach(p -> p.getValue().disconnect());
    }
}
