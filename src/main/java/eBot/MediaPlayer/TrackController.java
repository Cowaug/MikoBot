package eBot.MediaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class TrackController extends AudioEventAdapter implements Controller {
    private final AudioPlayer player;
    private final ArrayList<AudioTrack> list;
    private int idx;
    private boolean loopOne = false;
    private boolean loopAll = false;
    private TextChannel textChannel = null;
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

    @Override
    public void queue(AudioTrack track) {
        list.add(track);
        if (textChannel != null)
            textChannel.sendMessage("*```md\n# Added \n" + track.getInfo().title + "```*").queue();

        if (!hasTrack && player.getPlayingTrack() == null) {
            hasTrack = true;
            idx = 0;
            currentTrack = track.makeClone();
            notifyNewSong();
            player.startTrack(currentTrack, false);
        }
    }

    @Override
    public List<AudioTrack> getQueue() {
        return list;
    }

    @Override
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

    @Override
    public void jumpTo(int newIdx) {
        if (idx >= list.size())
            return;
        idx = newIdx;
        currentTrack = list.get(idx).makeClone();
        player.startTrack(currentTrack, false);
        hasTrack = false;
        notifyNewSong();
    }

    @Override
    public void setLoopOne() {
        loopOne = true;
        loopAll = false;
    }

    @Override
    public void setLoopAll() {
        loopOne = false;
        loopAll = true;
    }

    @Override
    public void setLoopOff() {
        loopAll = false;
        loopOne = false;
    }

    @Override
    public void pause() {
        if (!player.isPaused()) player.setPaused(true);
    }

    public void resume() {
        if (player.isPaused()) player.setPaused(false);
    }

    @Override
    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    @Override
    public void clear() {
        while (!list.isEmpty()) list.remove(0);
    }

    @Override
    public void stop() {
        clear();
        player.startTrack(null, false);
        hasTrack = false;
    }

    @Override
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

    @Override
    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    private void notifyNewSong() {
        if (this.textChannel != null)
            this.textChannel.sendMessage("***```md\n# Playing\n " + idx + ". [" + currentTrack.getInfo().title + "][" + toMin(currentTrack.getInfo().length) + "]```***").queue();
    }

    private String toMin(long l) {
        return (l / 1000) / 60 + ":" + (l / 1000) % 60;
    }
}