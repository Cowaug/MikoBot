package MikoBot.MediaPlayer;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class MapMessageIDChannel {
    private static ArrayList<Mapping> mapping = new ArrayList<>();

    public static String getBotLastMessageId(TextChannel textChannel) {
        Mapping tmp = getMapping(textChannel);
        if (tmp == null) return null;
        return tmp.getBotLastMessageId();
    }

    public static boolean editable(TextChannel textChannel) {
        Mapping tmp = getMapping(textChannel);
        if (tmp == null) return false;
        return tmp.editable();
    }

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

    public Mapping(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public void setBotLastMessageId(String messageId) {
        botLastMessageId = messageId;
        currentMessageId = messageId;
    }

    public void setCurrentMessageId(String messageId) {
        currentMessageId = messageId;
    }

    public String getBotLastMessageId() {
        return botLastMessageId;
    }

    public String getCurrentMessageId() {
        return currentMessageId;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public boolean editable() {
        return botLastMessageId.equals(currentMessageId);
    }
}