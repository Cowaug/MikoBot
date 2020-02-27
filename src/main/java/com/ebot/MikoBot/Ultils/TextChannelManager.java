package com.ebot.MikoBot.Ultils;

import com.ebot.MikoBot.BotInstance;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TextChannelManager {
    /**
     * React to message of user
     * @param event Event which user enter command
     * @param unicode Unicode from of emoji
     */
    public static void react(MessageReceivedEvent event, String unicode) {
        if (!event.getAuthor().isBot()) {
            event.getTextChannel().addReactionById(event.getMessageId(), EmojiParser.parseToUnicode(unicode)).queue();
        }
    }

    /**
     * Edit Bot's message
     * @param botInstance Bot Instance
     * @param event Event which Bot's send message
     * @param message Message content to override
     */
    public static void updateMessage(BotInstance botInstance, MessageReceivedEvent event, String message) {
        try {
            if (botInstance.isLastSentByBot(event))
                event.getTextChannel().editMessageById(botInstance.getBotsLastMessageId(event), message).queue();
            else {
                event.getTextChannel().sendMessage(message).queue();
                try {
                    botInstance.getLastTextChannel(event).deleteMessageById(botInstance.getBotsLastMessageId(event)).queue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            event.getTextChannel().sendMessage(message).queue();
        }

    }

    /**
     * Get info about Music Bot
     * @return Info of Music Bot
     */
    public static String getInfoMusic(){
        return ">>> " +
                "*Made by <@402414887264845825> at eBot Team*\n\n" +
                "``/play <Youtube link>`` Add song to queue\n"+
                "``/play <number>`` Play song in queue\n"+
                "``/stop`` Stop and clear the queue\n"+
                "``/next`` Play next song\n"+
                "``/loopAll`` | ``loopOne`` | ``loopOff`` Loop options\n"+
                "``/setVol <volume>`` Set bot volume (does not affect local setting)\n"+
                "``/page`` | ``page <number>`` Showing queued songs\n"+
                "``/pause`` | ``/resume`` Pause | resume playback\n"+
                "``/leave`` | ``/join`` Force bot leaves or joins room";
    }

    /**
     * Get info about TTS Bot
     * @return Info of TTS Bot
     */
    public static String getInfoTTS(){
        return ">>> \n" +
                "*Made by <@402414887264845825> at eBot Team*\n\n" +
                "``.. <text>`` Speak the text\n"+
                "``., <text>`` Speak the text then delete the message\n"+
                "``.lockme`` | ``.unlockme`` Auto speak the text you chat\n"+
                "``.skip`` Skip current speech\n"+
                "``.list`` View list of acronyms\n"+
                "``.leave`` Force bot leaves room";
    }
}
