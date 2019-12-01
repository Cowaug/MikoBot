package MikoBot;

import MikoBot.Listener.MessageListener;
import MikoBot.Listener.TTSListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;

import javax.security.auth.login.LoginException;

public class BotInstance {
    private static final String MUSIC = "Music";
    private static final String TTS = "Tts";
    private JDA jda;
    private String token;
    private String mode;
    private MessageListener messageListener = new TTSListener();

    public BotInstance(String token, String mode) {
        this.token = token;
        this.mode = mode;
        start();
    }

    private void start() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(token)
                    .setBulkDeleteSplittingEnabled(false)
                    .setCompression(Compression.NONE)
                    .setActivity(Activity.playing(mode.equals(MUSIC) ? "Music" : "TTS"))
                    .build();
            jda.addEventListener(messageListener);

        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        jda.shutdownNow();
    }

    public void restart() {
        shutdown();
        start();
    }
}