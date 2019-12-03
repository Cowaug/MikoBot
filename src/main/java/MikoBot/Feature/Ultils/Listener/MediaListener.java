package MikoBot.Feature.Ultils.Listener;

import MikoBot.Feature.MediaPlayback;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MediaListener extends MessageListener {
    private MediaPlayback mediaPlayback = new MediaPlayback();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
            mediaPlayback.start(event);
    }
}
