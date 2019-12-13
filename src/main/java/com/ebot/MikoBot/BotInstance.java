package com.ebot.MikoBot;

import com.ebot.MikoBot.Feature.Ultils.Listener.MediaListener;
import com.ebot.MikoBot.Feature.Ultils.Listener.MessageListener;
import com.ebot.MikoBot.Feature.Ultils.Listener.TTSListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;

import javax.security.auth.login.LoginException;

public class BotInstance {
    public static final String MUSIC = "Music";
    public static final String TTS = "TTS";
    private JDA jda;
    private String token;
    private String mode;
    private String region;
    private MessageListener ttsListener = new TTSListener();
    private MessageListener mediaListener = new MediaListener();

    public BotInstance(String token, String mode, String region) {
        this.token = token;
        this.mode = mode;
        this.region = region;
        build();
        setMode(mode);
    }

    private void build() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.playing(mode + " @" + region))
                    .build();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMode(String mode) {
        switch (mode) {
            case MUSIC:
                jda.removeEventListener(ttsListener);
                jda.addEventListener(mediaListener);
                break;
            case TTS:
                jda.removeEventListener(mediaListener);
                jda.addEventListener(ttsListener);
                break;
        }
    }

    public String getId() {
        return jda.getSelfUser().getId();
    }

    public void shutdown() {
        jda.shutdownNow();
    }

    public String getMode() {
        return mode;
    }

    public void restart() {
        shutdown();
        build();
    }
}