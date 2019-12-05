package MikoBot.Feature.Ultils.MediaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class TrackController extends AudioEventAdapter {
    AudioPlayer player;
    Queue queue;
    private boolean loopOne = false;
    private boolean loopAll = false;
    private TextChannel textChannel = null;
    private boolean lock = false;
    private int oldPage = 0;


    /**
     * Create the track scheduler for a player
     *
     * @param player The audio player this scheduler uses
     */
    TrackController(AudioPlayer player) {
        this.player = player;
        this.queue = new Queue();
    }

    /**
     * Add the track to queue and play it if nothing is playing
     *
     * @param track Audio track to queue
     */
    public void queue(AudioTrack track) {
        queue.add(track);

//        if (textChannel != null)
//            textChannel.sendMessage("*```md\n# Added \n" + track.getInfo().title + "```*").queue();

        if (!lock && player.getPlayingTrack() == null) {
            lock = true;
            player.startTrack(queue.getNext(), false);
        }
        sendMessage(oldPage);
    }

    public void queueList(AudioPlaylist audioPlaylist) {
        for (AudioTrack track : audioPlaylist.getTracks()) {
            queue.add(track);
        }
        if (!lock && player.getPlayingTrack() == null) {
            lock = true;
            player.startTrack(queue.getNext(), false);
        }
        sendMessage(oldPage);

    }

    public void queueList(ArrayList<AudioTrack> audioPlaylist) {
        for (AudioTrack track : audioPlaylist) {
            queue.add(track);
        }
        if (!lock && player.getPlayingTrack() == null) {
            lock = true;
            player.startTrack(queue.getNext(), false);
        }
        sendMessage(oldPage);

    }

    /**
     * Get all of the audio track in queue
     */
    public void getQueue(int page) {
        if (page == -1) sendMessage(queue.getCurrentIndex() / 10);
        else sendMessage(page);
    }

    /**
     * Force the player to play the next track
     * Playing the null track will stop the player
     */
    public void nextTrack(boolean notify) {
        if (loopOne) {
            player.startTrack(queue.getCurrent(), false);
        } else if (loopAll) {
            player.startTrack(queue.getNextLoop(), false);
        } else {
            AudioTrack audioTrack = queue.getNext();
            if (audioTrack == null) {
                player.startTrack(null, false);
                lock = false;
            } else player.startTrack(audioTrack, false);
        }
        if(notify) sendMessage(oldPage);
    }

    /**
     * Play the i-th track in queue
     *
     * @param newIdx index of track in queue
     */
    public void jumpTo(int newIdx) {
        player.startTrack(queue.get(newIdx), false);
        sendMessage(oldPage);
    }

    /**
     * Looping the current track
     */
    public void setLoopOne() {
        loopOne = true;
        loopAll = false;
        sendMessage(oldPage);
    }

    /**
     * Looping all track
     */
    public void setLoopAll() {
        loopOne = false;
        loopAll = true;
        sendMessage(oldPage);
    }

    /**
     * Just play one time then stop
     */
    public void setLoopOff() {
        loopAll = false;
        loopOne = false;
        sendMessage(oldPage);
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
        queue.clearAll();
    }

    /**
     * Stop and clear the queue
     */
    public void stop() {
        clear();
        player.startTrack(null, false);
        lock = false;
    }

    /**
     * Remove n-th track
     *
     * @param i position of track in queue
     */
    public void remove(int i) {
        if(i==queue.getCurrentIndex()) nextTrack(false);
        queue.remove(i);
        sendMessage(oldPage);
    }

    /**
     * Set text channel for notification
     *
     * @param textChannel text channel to send message
     */
    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            oldPage = queue.getCurrentIndex() / 10;
            nextTrack(true);
        }
    }

    private ArrayList<String> getPlayingList() {
        int i = 0;
        final String start = ">>> ```fix\n"
                + (loopAll ? "Loop All Song\n" : loopOne ? "Loop Current Song\n" : "Loop Off\n")
                + "PLAYING = "
                + (queue.getCurrentIndex() + 1)
                + ". "
                + queue.getCurrent().getInfo().title
                + "```\n```md\n";

        StringBuilder output = new StringBuilder(start
                + "# PAGE "
                + (i / 10 + 1)
                + "/"
                + (queue.getSize() / 10 + 1)
                + " #"
                + "\n");

        ArrayList<AudioTrack> list = queue.getList();
        ArrayList<String> strings = new ArrayList<>();
        for (; i < list.size(); i++) {
            AudioTrack audioTrack = list.get(i);
            output.append(i + 1)
                    .append(".")
                    .append((i + 1 <= 9) ? " " : "")
                    .append(" < ")
                    .append(toMin(audioTrack.getInfo().length))
                    .append(" > ")
                    .append(i == queue.getCurrentIndex() ? "* " : "  ")
                    .append(audioTrack.getInfo().title)
                    .append(i == queue.getCurrentIndex() ? " *" : "  ")
                    .append('\n');

            if (i % 10 == 9) {
                if (i != list.size() - 1) {
                    strings.add(output + "\n and " + (list.size() - i) + " more!```");
                } else strings.add(output + "```");

                output = new StringBuilder(start
                        + "# PAGE "
                        + (i / 10 + 2)
                        + "/"
                        + (queue.getSize() / 10 + 1)
                        + " #"
                        + "\n");
            }
        }
        strings.add(output.toString().equals("```md\n") ? "" : output + "```");
        return strings;
    }

    private String toMin(long l) {
        long hour = (l / 1000) / 60 / 60;
        String min = "0" + (l / 1000) / 60 % 60;
        String sec = "0" + (l / 1000) % 60;

        String ret = min.substring(min.length() - 2) + ":" + sec.substring(sec.length() - 2);
        if (hour > 0) return hour + ":" + ret;
        else return ret;
    }

    private void sendMessage(int page) {
        try {
            String lastMessageId = MapMessageIDChannel.getBotLastMessageId(textChannel);
            if (lastMessageId != null)
                if (MapMessageIDChannel.editable(textChannel)) {
//                getPlayingList().forEach(s ->  this.textChannel.editMessageById(lastMessageId,s).queue());
                    this.textChannel.editMessageById(lastMessageId, getPlayingList().get(page)).queue();

                } else {
                    this.textChannel.deleteMessageById(lastMessageId).queue();
//                getPlayingList().forEach(s -> this.textChannel.sendMessage(s).queue());
                    this.textChannel.sendMessage(getPlayingList().get(page)).queue();
                }
            else {
//            getPlayingList().forEach(s -> this.textChannel.sendMessage(s).queue());
                this.textChannel.sendMessage(getPlayingList().get(page)).queue();
            }
            oldPage = page;
        } catch (IndexOutOfBoundsException ignored) {

        }
    }
}