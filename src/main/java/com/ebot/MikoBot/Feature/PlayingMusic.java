package com.ebot.MikoBot.Feature;

import com.ebot.MikoBot.BotInstance;
import com.ebot.MikoBot.Ultils.MediaPlayer.MediaInstance;
import com.ebot.MikoBot.Ultils.TextChannelManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

import static com.ebot.MikoBot.Ultils.TextChannelManager.react;

public class PlayingMusic {
    static String MEDIA_PREFIX = "/";
    private TextChannel textChannel;
    private MediaInstance mediaInstance;
    private BotInstance botInstance;

    public PlayingMusic(BotInstance botInstance) {
        this.botInstance = botInstance;
    }

    /**
     * Start the media playback base on user request
     *
     * @param event User event which trigger this
     */
    public void start(MessageReceivedEvent event) {
        Member member = event.getMember();
        assert member != null;

        textChannel = event.getTextChannel();

        String[] message = event.getMessage().getContentDisplay().split("\n");

        new Thread(() -> {
            mediaInstance = botInstance.get(event);
            VoiceChannel voiceChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();

            for (String s : message) {
                if (!s.startsWith(MEDIA_PREFIX)) {
                    continue;
                }
                String content = s.substring(1);

                String cmd = content.substring(0, content.contains(" ") ? content.indexOf(" ") : content.length());

                content = content.replaceFirst(cmd, "").replace(" ", "");
                mediaInstance.getController().setLastEvent(botInstance, event);

                if (mediaInstance != null && voiceChannel != null) {
                    if(!cmd.equals("info")) mediaInstance.reconnect(voiceChannel);

                    try {
                        switch (cmd) {
                            case "play":
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
                            case "remove":

                                int i;
                                if (!content.equals("")) {
                                    if ((i = Integer.parseInt(content)) > 0)
                                        mediaInstance.getController().remove(i - 1);
                                    break;
                                } else {
                                    react(event, ":x:");
                                    return;
                                }

                            case "setVol":
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
                            case "stop":
                                mediaInstance.getController().stop();
                                break;
                            case "next":
                                mediaInstance.getController().nextTrack(true);
                                break;
                            case "loopOne":
                                mediaInstance.getController().setLoopOne();
                                break;
                            case "loopAll":
                                mediaInstance.getController().setLoopAll();
                                break;
                            case "loopOff":
                                mediaInstance.getController().setLoopOff();
                                break;
                            case "clear":
                                mediaInstance.getController().clear();
                                break;
                            case "pause":
                                mediaInstance.getController().pause();
                                break;
                            case "resume":
                                mediaInstance.getController().resume();
                                break;
                            case "leave":
                                mediaInstance.disconnect();
                                break;
                            case "join":
                                try {
                                    mediaInstance.reconnect(voiceChannel);
                                    mediaInstance.getController().pause();
                                    mediaInstance.getController().resume();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    react(event, ":x:");
                                    return;
                                }
                                break;
//                            case "reboot_":
////                                MainClass.reboot(MUSIC);
////                                break;
                            case "info":
                                TextChannelManager.updateMessage(botInstance,event,TextChannelManager.getInfoMusic());
                                break;
                            case "page":
                            case "queue":

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

                            default:
                                react(event, ":x:");
                                return;
                        }
                        react(event, ":ok_hand:");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        react(event, ":boom:");
                        return;
                    }

                } else {
                    react(event, ":x:");
                }

            }
        }).start();
    }
}