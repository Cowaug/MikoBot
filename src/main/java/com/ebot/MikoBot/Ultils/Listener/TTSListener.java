package com.ebot.MikoBot.Ultils.Listener;

import com.ebot.MikoBot.BotInstance;
import com.ebot.MikoBot.Feature.TextToSpeech;
import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class TTSListener extends MessageListener{
    private TextToSpeech textToSpeech;

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
