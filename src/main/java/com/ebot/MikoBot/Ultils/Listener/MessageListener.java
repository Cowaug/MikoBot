package com.ebot.MikoBot.Ultils.Listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


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
//        } else {
//            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
//                    event.getTextChannel().getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(),
//                    event.getMessage().getContentDisplay());
//        }
    }
}