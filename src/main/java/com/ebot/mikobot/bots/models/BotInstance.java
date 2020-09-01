package com.ebot.mikobot.bots.models;

import com.ebot.mikobot.features.mediaplayer.listener.MediaListener;
import com.ebot.mikobot.features.tts.listener.TTSListener;
import com.ebot.mikobot.features.mediaplayer.model.MediaInstance;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.Compression;

import java.util.*;

import static com.ebot.mikobot.bots.models.BotType.*;

public class BotInstance {
    private JDA jda;
    private String token;
    private BotType mode;
    private String region;
    private ArrayList<GuildInstance> guildInstances = new ArrayList<>();

    /**
     * Create Bot Instance
     * @param token Bot secret token
     * @param mode Bot's mode (MUSIC / TTS)
     * @param region Bot's Region (base on token on HEROKU)
     */
    public BotInstance(String token, BotType mode, String region) {
        System.out.println("CREATING " + mode + " BOT...");
        this.token = token;
        this.mode = mode;
        this.region = region;

        // Building bot base on token
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.playing(mode + " - v" + getVersion() + "." + region.substring(0, 2).toUpperCase()))
                    .build().awaitReady();

            // Add listener base on provided mode
            switch (mode) {
                case MUSIC:
                    jda.addEventListener(new MediaListener(this));
                    break;
                case TTS:
                    jda.addEventListener(new TTSListener(this));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get version of Bot base on Bot's Build Date
     * @return Bot's Start Date + Region
     */
    private String getVersion() {
        long time = System.currentTimeMillis();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        //Set time in milliseconds
        c.setTimeInMillis(time);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(mYear).substring(2) + "." +
                (mMonth < 10 ? "0" : "") + mMonth + "." +
                (mDay < 10 ? "0" : "") + mDay;
    }


    public MediaInstance getMediaInstance(MessageReceivedEvent event) {
        GuildInstance guildInstance;

        //create guildInstance if not exist
        if ((guildInstance = findGuild(event.getGuild())) == null) {
            guildInstances.add(guildInstance = new GuildInstance(event, this.jda));
        } else {
            guildInstance.update(event, this.jda);
        }

        //create mediaInstance if not exist in guildInstance
        if (guildInstance.getMediaInstance() == null) {
            guildInstance.setMediaInstance(new MediaInstance(event.getGuild(), mode));
            return guildInstance.getMediaInstance();
        } else {
            return guildInstance.getMediaInstance();
        }
    }

    /**
     * Find the MediaPlayer of specific server
     * @param guild Server
     * @return Pair of the server and it's MediaPlayer
     */
    private GuildInstance findGuild(final Guild guild) {
        return guildInstances.stream().filter(p -> p.getGuild().equals(guild)).findAny().orElse(null);
    }

    public boolean isLastSentByBot(MessageReceivedEvent event) {
        GuildInstance guildInstance = findGuild(event.getGuild());
        if (guildInstance == null || !guildInstance.getLastTextChannel().equals(event.getTextChannel())) return false;
        return guildInstance.isLastSendByBot();
    }

    public String getBotsLastMessageId(MessageReceivedEvent event) {
        GuildInstance guildInstance = findGuild(event.getGuild());
        if (guildInstance == null) return null;
        return guildInstance.getBotsLastMessageId();
    }

    public TextChannel getLastTextChannel(MessageReceivedEvent event) {
        GuildInstance guildInstance = findGuild(event.getGuild());
        if (guildInstance == null) return null;
        return guildInstance.getLastTextChannel();
    }
}






