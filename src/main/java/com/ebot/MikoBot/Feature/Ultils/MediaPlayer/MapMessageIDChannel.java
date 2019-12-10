package com.ebot.MikoBot.Feature.Ultils.MediaPlayer;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

/**
 * Create an array to save
 * channel's last messageId and bot's last messageId
 * on that channel for future edit/delete
 */
public class MapMessageIDChannel {
    private static ArrayList<Mapping> mapping = new ArrayList<>();

    /**
     * Get bot's last messageId on an text channel
     *
     * @param textChannel Text channel
     * @return messageId
     */
    static String getBotLastMessageId(TextChannel textChannel) {
        Mapping tmp = getMapping(textChannel);
        if (tmp == null) return null;
        return tmp.getBotLastMessageId();
    }

    /**
     * Check if last message was sent by bot or user
     *
     * @param textChannel Text channel
     * @return is the last message was sent by this bot
     */
    static boolean editable(TextChannel textChannel) {
        Mapping tmp = getMapping(textChannel);
        if (tmp == null) return false;
        return tmp.editable();
    }

    /**
     * @param textChannel Text channel to find
     * @return Mapping object of text channel, botLastMessId, lastMessId
     */
    private static Mapping getMapping(TextChannel textChannel) {
        return mapping.stream().filter(p -> p.getTextChannel().equals(textChannel)).findAny().orElse(null);
    }

    public static void setBotLastMessageId(TextChannel textChannel, String messageId) {
        Mapping tmp = getMapping(textChannel);
        if (tmp == null) {
            Mapping map = new Mapping(textChannel);
            map.setBotLastMessageId(messageId);
            mapping.add(map);
        }
        else tmp.setBotLastMessageId(messageId);
    }

    public static void setCurrentMessageId(TextChannel textChannel, String messageId) {
        Mapping tmp = getMapping(textChannel);
        if (tmp == null) {
            Mapping map = new Mapping(textChannel);
            map.setCurrentMessageId(messageId);
            mapping.add(map);
        }
        else tmp.setCurrentMessageId(messageId);
    }
}

class Mapping {
    private TextChannel textChannel;
    private String botLastMessageId = null;
    private String currentMessageId = null;

    Mapping(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    void setBotLastMessageId(String messageId) {
        botLastMessageId = messageId;
        currentMessageId = messageId;
    }

    void setCurrentMessageId(String messageId) {
        currentMessageId = messageId;
    }

    String getBotLastMessageId() {
        return botLastMessageId;
    }

    TextChannel getTextChannel() {
        return textChannel;
    }

    boolean editable() {
        return botLastMessageId.equals(currentMessageId);
    }
}