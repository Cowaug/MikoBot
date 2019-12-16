package com.ebot.MikoBot.Feature.Ultils.Listener;

import com.ebot.MikoBot.Feature.MediaPlayback;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MediaListener extends MessageListener {
    private MediaPlayback mediaPlayback;

    public MediaListener(String botId){
        mediaPlayback=new MediaPlayback(botId);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("MUSIC");
        super.onMessageReceived(event);
        mediaPlayback.start(event);
    }
}
