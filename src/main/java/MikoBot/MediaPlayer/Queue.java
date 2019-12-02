package MikoBot.MediaPlayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;

public class Queue {
    private ArrayList<AudioTrack> audioTracks = new ArrayList<>();
    private int currentIndex = -1;

    void add(AudioTrack audioTrack) {
        if (audioTrack == null) return;
        audioTracks.add(audioTrack);
    }

    AudioTrack getNext() {
        currentIndex++;
        if (currentIndex < audioTracks.size())
            return audioTracks.get(currentIndex).makeClone();

        currentIndex = audioTracks.size();
        return null;
    }

    AudioTrack getCurrent() {
        if (currentIndex >= audioTracks.size()) return null;
        return audioTracks.get(currentIndex).makeClone();
    }

    AudioTrack getNextLoop() {
        currentIndex++;
        if (currentIndex >= audioTracks.size())
            currentIndex = 0;
        return audioTracks.get(currentIndex).makeClone();
    }

    public AudioTrack pull() {
        if (audioTracks.size() <= 0) return null;
        AudioTrack audioTrack = audioTracks.get(0);
        audioTracks.remove(0);
        return audioTrack.makeClone();
    }

    AudioTrack get(int index) {
        currentIndex = index - 1;
        return getNext();
    }

    void remove(int index) {
        audioTracks.remove(index);
        if (currentIndex >= index)
            currentIndex--;
    }

    void clearAll() {
        audioTracks.clear();
        currentIndex = -1;
    }

    ArrayList<AudioTrack> getList() {
        return audioTracks;
    }

    int getCurrentIndex(){
        return currentIndex;
    }
    int getSize(){
        return audioTracks.size();
    }
}
