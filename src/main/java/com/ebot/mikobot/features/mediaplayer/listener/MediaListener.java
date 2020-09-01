package com.ebot.mikobot.features.mediaplayer.listener;

import com.ebot.mikobot.bots.models.BotInstance;
import com.ebot.mikobot.features.mediaplayer.PlayingMusic;
import com.ebot.mikobot.ultils.MessageListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class MediaListener extends MessageListener {
    private PlayingMusic playingMusic;

    /**
     * Create Media Listener with Bot instance
     * @param botInstance Bot Instance (created with token)
     */
    public MediaListener(BotInstance botInstance){
        playingMusic = new PlayingMusic(botInstance);
    }

    @Override
    public void onMessageReceived( MessageReceivedEvent event) {
        super.onMessageReceived(event);
        playingMusic.execute(event);
    }
}
