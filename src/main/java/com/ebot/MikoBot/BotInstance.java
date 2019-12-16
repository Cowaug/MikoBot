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
    private MessageListener ttsListener = null;
    private MessageListener mediaListener = null;

    BotInstance(String token, String mode, String region) {
        System.out.println("CREATING " + mode.toUpperCase() + " BOT...");
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
                    .build().awaitReady();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setMode(String mode) {
        switch (mode) {
            case MUSIC:
                //jda.removeEventListener(ttsListener);
                if (mediaListener == null) mediaListener = new MediaListener(jda.getSelfUser().getId());
                jda.addEventListener(mediaListener);
                break;
            case TTS:
                //jda.removeEventListener(mediaListener);
                if (ttsListener == null) ttsListener = new TTSListener();
                jda.addEventListener(ttsListener);
                break;
        }
    }

    private void shutdown() {
        jda.shutdownNow();
    }

    public void restart() {
        shutdown();
        build();
    }
}