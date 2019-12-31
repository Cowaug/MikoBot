package com.ebot.MikoBot.Ultils.Entities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;

public class Queue {
    private ArrayList<AudioTrack> audioTracks = new ArrayList<>();
    private int currentIndex = -1;

    public void add(AudioTrack audioTrack) {
        if (audioTrack == null) return;
        audioTracks.add(audioTrack);
    }

    public AudioTrack getNext() {
        currentIndex++;
        if (currentIndex < audioTracks.size())
            return audioTracks.get(currentIndex).makeClone();
        if (audioTracks.size() == 0)
            currentIndex = -1;
        else currentIndex = audioTracks.size();
        return null;
    }

    public AudioTrack getCurrent() {
        try{
            return audioTracks.get(currentIndex).makeClone();
        } catch (Exception ignored){
            return null;
        }
    }

    public AudioTrack getNextLoop() {
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

    public AudioTrack get(int index) {
        currentIndex = index - 1;
        return getNext();
    }

    public void remove(int index) {
        audioTracks.remove(index);
        if (currentIndex >= index)
            currentIndex--;
    }

    public void clearAll() {
        audioTracks.clear();
        currentIndex = -1;
    }

    public ArrayList<AudioTrack> getList() {
        return audioTracks;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getSize() {
        return audioTracks.size();
    }
}
