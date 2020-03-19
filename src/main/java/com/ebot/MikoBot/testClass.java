package com.ebot.MikoBot;


import com.ebot.MikoBot.Ultils.Entities.Commands;
import com.ebot.MikoBot.Ultils.MediaPlayer.TtsController;
import com.sedmelluq.discord.lavaplayer.filter.PcmFilterFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

/**
 * This for testing only - it serves nothing
 */
public class testClass {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
    }
}
