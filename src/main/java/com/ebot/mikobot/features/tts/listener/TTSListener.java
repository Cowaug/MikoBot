package com.ebot.mikobot.features.tts.listener;

import com.ebot.mikobot.bots.models.BotInstance;
import com.ebot.mikobot.features.tts.TextToSpeech;
import com.ebot.mikobot.ultils.MessageListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class TTSListener extends MessageListener {
    private final TextToSpeech textToSpeech;

    /**
     * Create TTS Listener with Bot instance
     * @param botInstance Bot Instance (created with token)
     */
    public TTSListener(BotInstance botInstance){
        textToSpeech = new TextToSpeech(botInstance);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(!event.getAuthor().isBot())
        textToSpeech.execute(event);
    }
}
