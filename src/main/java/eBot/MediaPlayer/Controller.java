package eBot.MediaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public interface Controller {

    /**
     * Add the track to queue and play it if nothing is playing
     * @param track Audio track to queue
     */
    void queue(AudioTrack track);

    /**
     * Get all of the audio track in queue
     * @return List of AudioTrack
     */
    List<AudioTrack> getQueue();

    /**
     * Force the player to play the next track
     * Playing the null track will stop the player
     */
    void nextTrack();

    /**
     * Play the i-th track in queue
     * @param idx index of track in queue
     */
    void jumpTo(int idx);

    /**
     * Looping the current song
     */
    void setLoopOne();

    /**
     * Looping all song
     */
    void setLoopAll();

    /**
     * Just play one time then stop
     */
    void setLoopOff();

    /**
     * Pause the player if it is playing
     */
    void pause();

    /**
     * Resume the player if it is paused
     */
    void resume();

    /**
     * Set volume of the player
     * @param volume Volume level
     */
    void setVolume(int volume);

    /**
     * Clear the remaining queue
     */
    void clear();

    /**
     * Stop and clear the queue
     */
    void stop();

    /**
     * Remove n-th track
     * @param i position of track in queue
     */
    void remove(int i);

    /**
     * Set text channel for notification
     * @param textChannel text channel to send message
     */
    void setTextChannel(TextChannel textChannel);
}
