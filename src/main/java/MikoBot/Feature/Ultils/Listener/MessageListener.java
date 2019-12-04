package MikoBot.Feature.Ultils.Listener;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;


public class MessageListener extends ListenerAdapter {

    /**
     * Action when receive a message
     *
     * @param event Message received event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
//        if (event.isFromType(ChannelType.PRIVATE)) {
//            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
//                    event.getMessage().getContentDisplay());
//            console.update("[PM] " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay() + "\n");
//        } else {
//            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
//                    event.getTextChannel().getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(),
//                    event.getMessage().getContentDisplay());
//            console.update("[" + event.getGuild().getName() + "][" + event.getTextChannel().getName() + "]" +Objects.requireNonNull(event.getMember()).getEffectiveName() + ": " + event.getMessage().getContentDisplay() + "\n");
//        }
    }
}
