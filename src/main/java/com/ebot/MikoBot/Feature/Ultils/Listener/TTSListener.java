package com.ebot.MikoBot.Feature.Ultils.Listener;

import com.ebot.MikoBot.Feature.TextToSpeech;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TTSListener extends MessageListener{
    private TextToSpeech textToSpeech = new TextToSpeech();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(!event.getAuthor().isBot())
        textToSpeech.start(event,TextToSpeech.GOOGLE_TRANSLATE);
    }
}
