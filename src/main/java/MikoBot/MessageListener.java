package MikoBot;

import MikoBot.Feature.MediaPlayback;
import MikoBot.Feature.TextToSpeech;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import static MikoBot.Run.*;


public class MessageListener extends ListenerAdapter {
    private static TextToSpeech textToSpeech = new TextToSpeech();
    private static MediaPlayback mediaPlayback = new MediaPlayback();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());


        } else {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                    event.getTextChannel().getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(),
                    event.getMessage().getContentDisplay());

            String message = event.getMessage().getContentDisplay();

            switch (MODE) {
                case TTS:
                    textToSpeech.start(event, TextToSpeech.GOOGLE_TRANSLATE);
                    break;
                case MUSIC:
                    if (message.startsWith(MEDIA_PREFIX))
                        mediaPlayback.start(event);
                    break;
            }
        }
    }
}
