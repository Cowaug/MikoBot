package com.ebot.MikoBot.Ultils.Entities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;

public class Queue {
    private ArrayList<AudioTrack> audioTracks = new ArrayList<>();
    private int currentIndex = -1;

    /**
     * Add audio track to queue
     * @param audioTrack Audio Track
     */
    public void add(AudioTrack audioTrack) {
        if (audioTrack == null) return;
        audioTracks.add(audioTrack);
    }

    /**
     * Get next Audio Track with loop disable
     * @return Next Audio Track in queue (null if end of queue)
     */
    public AudioTrack getNext() {
        currentIndex++;
        if (currentIndex < audioTracks.size())
            return audioTracks.get(currentIndex).makeClone();
        if (audioTracks.size() == 0)
            currentIndex = -1;
        else currentIndex = audioTracks.size();
        return null;
    }

    /**
     * Get current Audio track
     * @return Current Audio track (null if error occurs)
     */
    public AudioTrack getCurrent() {
        try{
            return audioTracks.get(currentIndex).makeClone();
        } catch (Exception ignored){
            return null;
        }
    }

    /**
     * Get next Audio track with loop enable
     * @return Next Audio track (first track if end of queue)
     */
    public AudioTrack getNextLoop() {
        currentIndex++;
        if (currentIndex >= audioTracks.size())
            currentIndex = 0;
        return audioTracks.get(currentIndex).makeClone();
    }

    /**
     * Get next Audio track and remove it from queue
     * @return Next Audio track (null if end of queue)
     */
    public AudioTrack pull() {
        if (audioTracks.size() <= 0) return null;
        AudioTrack audioTrack = audioTracks.get(0);
        audioTracks.remove(0);
        return audioTrack.makeClone();
    }

    /**
     * Get Audio track with specific index
     * @param index Index of Audio track
     * @return Audio track (null if out of range)
     */
    public AudioTrack get(int index) {
        currentIndex = index - 1;
        return getNext();
    }

    /**
     * Remove Audio track with specific index
     * @param index Index of Audio track
     */
    public void remove(int index) {
        audioTracks.remove(index);
        if (currentIndex >= index)
            currentIndex--;
    }

    /**
     * Clear all Audio track in queue
     */
    public void clearAll() {
        audioTracks.clear();
        currentIndex = -1;
    }

    /**
     * Get list of Audio track
     * @return Array list of Audio track
     */
    public ArrayList<AudioTrack> getList() {
        return audioTracks;
    }

    /**
     * Get current index in queue
     * @return Index in queue
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Get number of queued Audio tracks
     * @return Number of queued Audio tracks
     */
    public int getSize() {
        return audioTracks.size();
    }
}