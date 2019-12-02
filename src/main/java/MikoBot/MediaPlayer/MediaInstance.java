package MikoBot.MediaPlayer;

import MikoBot.Run;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import static MikoBot.BotInstance.TTS;

public class MediaInstance {
    private AudioPlayerManager playerManager;
    private AudioManager audioManager;
    private TrackController controller;

    /**
     * Create specific media player for a server
     *
     * @param guild        server
     * @param voiceChannel voice channel to connect
     */
    public MediaInstance(Guild guild, VoiceChannel voiceChannel) {

        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new BeamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        AudioPlayer player = playerManager.createPlayer();

        if (Run.console.getMode().equals(TTS)) controller = new TtsController(player);
        else controller = new TrackController(player);

        player.addListener(controller);
        controller.setVolume(25);
        audioManager = guild.getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
        audioManager.openAudioConnection(voiceChannel);
    }

    /**
     * Try connecting to specific voice channel
     *
     * @param voiceChannel Voice channel to connect
     */
    public void reconnect(VoiceChannel voiceChannel) {
        audioManager.openAudioConnection(voiceChannel);
    }

    /**
     * Disconnect voice channel
     */
    public void disconnect() {
        audioManager.closeAudioConnection();
    }

    /**
     * Play the audio from target URL
     *
     * @param url URL that contain the audio
     */
    public void play(String url, TextChannel textChannel) {
        playerManager.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (textChannel != null) {

                    controller.queue(track);
                } else {
                    controller.setVolume(75);
                    controller.queue(track);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    controller.queue(track);
                }
            }

            @Override
            public void noMatches() {
                if (textChannel != null) textChannel.sendMessage("Nothing match: " + url).queue();
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                if (textChannel != null) textChannel.sendMessage("Load " + url + " failed").queue();
            }
        });
    }

    public TrackController getController() {
        return controller;
    }
}
