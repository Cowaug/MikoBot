package com.ebot.MikoBot;

import com.ebot.MikoBot.Ultils.Listener.MediaListener;
import com.ebot.MikoBot.Ultils.Listener.TTSListener;
import com.ebot.MikoBot.Ultils.MediaPlayer.MediaInstance;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.Compression;

import java.net.JarURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class BotInstance {
    public static final String MUSIC = "Music";
    public static final String TTS = "TTS";
    private JDA jda;
    private String token;
    private String mode;
    private String region;
    private ArrayList<GuildInstance> guildInstances = new ArrayList<>();

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
                    .setActivity(Activity.playing(mode + " - v" + getVersion() + "." + region.substring(0, 2).toUpperCase()))
                    .build().awaitReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMode(String mode) {
        switch (mode) {
            case MUSIC:
                jda.addEventListener(new MediaListener(this));
                break;
            case TTS:
                jda.addEventListener(new TTSListener(this));
                break;
        }
    }

    private static String getVersion() {
        long time = 0;
        try {
            String rn = MainClass.class.getName().replace('.', '/') + ".class";
            JarURLConnection j = (JarURLConnection) Objects.requireNonNull(MainClass.class.getClassLoader().getResource(rn)).openConnection();
            time = j.getJarFile().getEntry("META-INF/MANIFEST.MF").getTime();
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
                (mMonth < 10 ? "0" : "") + mMonth +
                (mDay < 10 ? "0" : "") + mDay;
    }

    public MediaInstance getMediaInstance(MessageReceivedEvent event) {
        GuildInstance guildInstance;

        //create guildInstance if not exist
        if ((guildInstance = findGuild(event.getGuild())) == null) {
            guildInstances.add(guildInstance = new GuildInstance(event));
        } else {
            guildInstance.update(event);
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
     *
     * @param guild Server
     * @return Pair of the server and it's MediaPlayer
     */
    private GuildInstance findGuild(final Guild guild) {
        return guildInstances.stream().filter(p -> p.getGuild().equals(guild)).findAny().orElse(null);
    }

    public boolean isLastSendByBot(MessageReceivedEvent event) {
        GuildInstance guildInstance = findGuild(event.getGuild());
        if (guildInstance == null || !guildInstance.getLastTextChannel().equals(event.getTextChannel())) return false;
        return guildInstance.isLastSendByBot();
    }

    public String getLastBotsMessageId(MessageReceivedEvent event) {
        GuildInstance guildInstance = findGuild(event.getGuild());
        if (guildInstance == null) return null;
        return guildInstance.getLastBotsMessageId();
    }

    public TextChannel getLastTextChannel(MessageReceivedEvent event) {
        GuildInstance guildInstance = findGuild(event.getGuild());
        if (guildInstance == null) return null;
        return guildInstance.getLastTextChannel();
    }
}

class GuildInstance {
    private Guild guild;
    private MediaInstance mediaInstance = null;
    private TextChannel lastTextChannel;
    private String lastBotsMessageId = null;
    private boolean isLastSendByBot = false;

    GuildInstance(MessageReceivedEvent event) {
        this.guild = event.getGuild();
        update(event);
    }

    Guild getGuild() {
        return guild;
    }

    MediaInstance getMediaInstance() {
        return mediaInstance;
    }

    void update(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(">>> ")) {
            this.lastBotsMessageId = event.getMessageId();
            this.lastTextChannel = event.getTextChannel();
            isLastSendByBot = true;
        } else isLastSendByBot = false;
    }

    void setMediaInstance(MediaInstance mediaInstance) {
        this.mediaInstance = mediaInstance;
    }

    boolean isLastSendByBot() {
        return isLastSendByBot;
    }

    String getLastBotsMessageId() {
        return lastBotsMessageId;
    }

    TextChannel getLastTextChannel() {
        return lastTextChannel;
    }
}






