package com.ebot.MikoBot.Feature;

import com.ebot.MikoBot.BotInstance;
import com.ebot.MikoBot.MainClass;
import com.ebot.MikoBot.Ultils.Entities.Commands;
import com.ebot.MikoBot.Ultils.MediaPlayer.MediaInstance;
import com.ebot.MikoBot.Ultils.TextChannelManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

import static com.ebot.MikoBot.BotInstance.MUSIC;
import static com.ebot.MikoBot.Ultils.TextChannelManager.react;

public class PlayingMusic {
    static String MEDIA_PREFIX = "/";
    private BotInstance botInstance;

    public PlayingMusic(BotInstance botInstance) {
        this.botInstance = botInstance;
    }

    /**
     * Start the media playback base on user request
     *
     * @param event User event which trigger this
     */
    public void execute(final MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentDisplay().split("\n");

        new Thread(() -> {

            TextChannel textChannel = event.getTextChannel();
            MediaInstance mediaInstance = botInstance.getMediaInstance(event);
            VoiceChannel voiceChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();

            for (String s : message) {
                if (!s.startsWith(MEDIA_PREFIX)) continue;

                try {
                    mediaInstance.getController().setLastEvent(botInstance, event);
                    String content = s.substring(1);
                    Commands.MUSIC cmd = Commands.MUSIC.valueOf(content.substring(0, content.contains(" ") ? content.indexOf(" ") : content.length()).toUpperCase());
                    content = content.toLowerCase().replaceFirst(cmd.toString().toLowerCase(), "").replace(" ", "");

                    if (voiceChannel != null || !cmd.needInVoice) {
                        if (cmd.needInVoice) mediaInstance.reconnect(voiceChannel);
                        switch (cmd) {
                            case PLAY:
                                if (!content.equals("")) {
                                    try {
                                        int customIdx = Integer.parseInt(content);
                                        mediaInstance.getController().jumpTo(customIdx - 1);
                                        break;
                                    } catch (Exception ignored) {
                                        mediaInstance.play(content, textChannel);
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                } else {
                                    react(event, ":x:");
                                    return;
                                }

                            case REMOVE:
                                int i;
                                if (!content.equals("")) {
                                    if ((i = Integer.parseInt(content)) > 0)
                                        mediaInstance.getController().remove(i - 1);
                                    break;
                                } else {
                                    react(event, ":x:");
                                    return;
                                }

                            case SETVOL:
                                int vol;
                                if (!content.equals("")) {
                                    if ((vol = Integer.parseInt(content)) > 0) {
                                        mediaInstance.getController().setVolume(vol);
                                    }
                                    break;
                                } else {
                                    react(event, ":x:");
                                    return;
                                }
                            case STOP:
                                mediaInstance.getController().stop();
                                break;
                            case NEXT:
                                mediaInstance.getController().nextTrack(true);
                                break;
                            case LOOPONE:
                                mediaInstance.getController().setLoopOne();
                                break;
                            case LOOPALL:
                                mediaInstance.getController().setLoopAll();
                                break;
                            case LOOPOFF:
                                mediaInstance.getController().setLoopOff();
                                break;
                            case CLEAR:
                                mediaInstance.getController().clear();
                                break;
                            case PAUSE:
                                mediaInstance.getController().pause();
                                break;
                            case RESUME:
                                mediaInstance.getController().resume();
                                break;
                            case LEAVE:
                                mediaInstance.disconnect();
                                break;
                            case JOIN:
                                try {
                                    mediaInstance.reconnect(voiceChannel);
                                    mediaInstance.getController().pause();
                                    mediaInstance.getController().resume();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    react(event, ":boom:");
                                    return;
                                }
                                break;
                            case INFO:
                                TextChannelManager.updateMessage(botInstance, event, TextChannelManager.getInfoMusic());
                                break;
                            case PAGE:
                            case QUEUE:
                                int page;
                                if (content.equals("")) {
                                    mediaInstance.getController().getQueue(-1);
                                    break;
                                } else if ((page = Integer.parseInt(content)) > 0) {
                                    mediaInstance.getController().getQueue(page - 1);
                                    break;
                                } else {
                                    react(event, ":x:");
                                    return;
                                }
                        }
                        react(event, ":ok_hand:");
                    } else {
                        react(event, ":headphones:");
                        react(event, ":exclamation:");
                    }
                }
                catch (IllegalArgumentException illegalArgs){
                    System.out.println(illegalArgs.getMessage());
                    react(event, ":question:");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    react(event, ":boom:");
                }
            }

        }).start();
    }
}