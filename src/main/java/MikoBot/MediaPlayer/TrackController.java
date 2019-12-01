package MikoBot.MediaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class TrackController extends AudioEventAdapter {
    private final AudioPlayer player;
    private final ArrayList<AudioTrack> list;
    private int idx;
    private boolean loopOne = false;
    private boolean loopAll = false;
    private TextChannel textChannel = null;
    private String oldMessageId = null;
    private AudioTrack currentTrack = null;
    private boolean hasTrack = false;

    /**
     * Create the track scheduler for a player
     *
     * @param player The audio player this scheduler uses
     */
    TrackController(AudioPlayer player) {
        this.player = player;
        this.list = new ArrayList<>();
    }

    /**
     * Add the track to queue and play it if nothing is playing
     *
     * @param track Audio track to queue
     */
    public void queue(AudioTrack track) {
        list.add(track);
        if (textChannel != null)
            textChannel.sendMessage("*```md\n# Added \n" + track.getInfo().title + "```*").queue();

        if (!hasTrack && player.getPlayingTrack() == null) {
            hasTrack = true;
            idx = 0;
            currentTrack = track.makeClone();
            player.startTrack(currentTrack, false);
            notifyNewSong();
        }
    }

    /**
     * Get all of the audio track in queue
     *
     */
    public void getQueue() {
        this.textChannel.sendMessage(getPlayingList()).queue();
    }

    /**
     * Force the player to play the next track
     * Playing the null track will stop the player
     */
    public void nextTrack() {

        if (loopOne) {
            currentTrack = list.get(idx).makeClone();
            player.startTrack(currentTrack, false);
            notifyNewSong();
        } else {
            idx++;
            if (loopAll) {
                idx = idx % list.size();
                currentTrack = list.get(idx).makeClone();
                player.startTrack(currentTrack, false);
                notifyNewSong();
            } else if (idx >= list.size()) {
                idx = 0;
                player.startTrack(null, false);
                hasTrack = false;
                if (this.textChannel != null)
                    this.textChannel.sendMessage("End of queue").queue();
            }
        }
    }

    /**
     * Play the i-th track in queue
     *
     * @param newIdx index of track in queue
     */
    public void jumpTo(int newIdx) {
        if (idx >= list.size())
            return;
        idx = newIdx;
        currentTrack = list.get(idx).makeClone();
        player.startTrack(currentTrack, false);
        hasTrack = false;
        notifyNewSong();
    }

    /**
     * Looping the current track
     */
    public void setLoopOne() {
        loopOne = true;
        loopAll = false;
    }

    /**
     * Looping all track
     */
    public void setLoopAll() {
        loopOne = false;
        loopAll = true;
    }

    /**
     * Just play one time then stop
     */
    public void setLoopOff() {
        loopAll = false;
        loopOne = false;
    }

    /**
     * Pause the player if it is playing
     */
    public void pause() {
        if (!player.isPaused()) player.setPaused(true);
    }

    /**
     * Resume the player if it is paused
     */
    public void resume() {
        if (player.isPaused()) player.setPaused(false);
    }

    /**
     * Set volume of the player
     *
     * @param volume Volume level
     */
    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    /**
     * Clear the remaining queue
     */
    public void clear() {
        while (!list.isEmpty()) list.remove(0);
    }

    /**
     * Stop and clear the queue
     */
    public void stop() {
        clear();
        player.startTrack(null, false);
        hasTrack = false;
    }

    /**
     * Remove n-th track
     *
     * @param i position of track in queue
     */
    public void remove(int i) {
        if (i >= list.size() || i < 0) return;
        if (idx == i) {
            if (list.size() == 1)
                stop();
            else {
                nextTrack();
                idx--;
            }
        }
        if (list.size() <= 1 && i == 0) list.clear();
        else list.remove(i);
    }

    /**
     * Set text channel for notification
     *
     * @param textChannel text channel to send message
     */
    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public void setMessageId(String messageId) {
        oldMessageId = messageId;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    private void notifyNewSong() {
        if (this.textChannel == null) return;

        StringBuilder output = new StringBuilder("***```md\n# Playing\n ").append(idx).append(". < ").append(toMin(currentTrack.getInfo().length)).append(" > || ").append(currentTrack.getInfo().title).append('\n').append("```***");
        //this.textChannel.editMessageById(oldMessageId,output).queue();
        this.textChannel.sendMessage(output.append(getPlayingList())).queue();
    }

    private StringBuilder getPlayingList() {
        StringBuilder output = new StringBuilder("```md\n");
        for (int i = 0; i < list.size(); i++) {
            AudioTrack audioTrack = list.get(i);
            output.append(i).append(". < ").append(toMin(audioTrack.getInfo().length)).append(" > || ").append(audioTrack.getInfo().title).append('\n');
        }
        output.append("```");
        return output;
    }

    private String toMin(long l) {
        long hour = (l / 1000) / 60 / 60;
        String min = "0" + (l / 1000) / 60 % 60;
        String sec = "0" + (l / 1000) % 60;

        String ret = min.substring(min.length() - 2) + ":" + sec.substring(sec.length() - 2);
        if (hour > 0) return hour + ":" + ret;
        else return ret;
    }
}