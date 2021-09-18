package com.ebot.mikobot.features.tts.controller;

import com.ebot.mikobot.features.mediaplayer.controller.TrackController;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 *  This controller inherit the controller from media player feature
 */
public class TtsController extends TrackController {
    /**
     * Create the track scheduler for a player
     *
     * @param player The audio player this scheduler uses
     */
    public TtsController(AudioPlayer player) {
        super(player);
    }

    @Override
    public void queue(AudioTrack track) {
        queue.add(track);

//        if (textChannel != null)
//            textChannel.sendMessage("*```md\n# Added \n" + track.getInfo().title + "```*").queue();

        if (player.getPlayingTrack() == null) {
            player.startTrack(queue.pull(), false);
        }
    }

    @Override
    public void nextTrack(boolean notify) {
        player.startTrack(queue.pull(),false);
    }
}
