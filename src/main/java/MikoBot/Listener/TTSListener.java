package MikoBot.Listener;

import MikoBot.Feature.TextToSpeech;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TTSListener extends MessageListener{
    private TextToSpeech textToSpeech = new TextToSpeech();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        textToSpeech.start(event,TextToSpeech.GOOGLE_TRANSLATE);
    }
}
