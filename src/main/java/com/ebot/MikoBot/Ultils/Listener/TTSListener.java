package com.ebot.MikoBot.Ultils.Listener;

import com.ebot.MikoBot.BotInstance;
import com.ebot.MikoBot.Feature.PlayingMusic;
import com.ebot.MikoBot.Feature.TextToSpeech;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TTSListener extends MessageListener{
    private TextToSpeech textToSpeech;

    public TTSListener(BotInstance botInstance){
        textToSpeech = new TextToSpeech(botInstance);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(!event.getAuthor().isBot())
        textToSpeech.start(event);
    }
}
