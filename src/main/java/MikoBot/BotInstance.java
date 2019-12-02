package MikoBot;

import MikoBot.Listener.MediaListener;
import MikoBot.Listener.MessageListener;
import MikoBot.Listener.TTSListener;
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
    private MessageListener ttsListener = new TTSListener();
    private MessageListener mediaListener = new MediaListener();

    public BotInstance(String token, String mode) {
        this.token = token;
        this.mode = mode;
        build();
        setMode(mode);
    }

    private void build() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.playing(mode))
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
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

    public String getId(){
        return jda.getSelfUser().getId();
    }

    public void shutdown() {
        jda.shutdownNow();
    }

    public String getMode(){
        return mode;
    }

    public void restart() {
        shutdown();
        build();
    }
}