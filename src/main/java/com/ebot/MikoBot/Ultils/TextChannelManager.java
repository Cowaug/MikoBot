package com.ebot.MikoBot.Ultils;

import com.ebot.MikoBot.BotInstance;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TextChannelManager {
    public static void react(MessageReceivedEvent event, String unicode) {
        if (!event.getAuthor().isBot()) {
            event.getTextChannel().addReactionById(event.getMessageId(), EmojiParser.parseToUnicode(unicode)).queue();
        }
    }

    public static void updateMessage(BotInstance botInstance, MessageReceivedEvent event, String message) {
        try {
            if (botInstance.isLastSendByBot(event))
                event.getTextChannel().editMessageById(botInstance.getLastBotsMessageId(event), message).queue();
            else {
                event.getTextChannel().sendMessage(message).queue();
                try {
                    botInstance.getLastTextChannel(event).deleteMessageById(botInstance.getLastBotsMessageId(event)).queue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            event.getTextChannel().sendMessage(message).queue();
        }

    }

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
    public static String getInfoTTS(){
        return ">>> \n" +
                "*Made by <@402414887264845825> at eBot Team*\n\n" +
                "``.. <text>`` Speak the text\n"+
                "``., <text>`` Speak the text then delete the message\n"+
                "``.lockme`` | ``.unlockme`` Auto speak the text you chat\n"+
                "``.skip`` Skip current speech\n"+
                "``/leave`` Force bot leaves room";
    }
}
