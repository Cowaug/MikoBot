package com.ebot.MikoBot.Ultils.Listener;

import com.ebot.MikoBot.BotInstance;
import com.ebot.MikoBot.Feature.PlayingMusic;
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
