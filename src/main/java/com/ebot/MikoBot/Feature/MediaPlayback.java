package com.ebot.MikoBot.Feature;

import com.ebot.MikoBot.Feature.Ultils.MediaPlayer.MapMessageIDChannel;
import com.ebot.MikoBot.Feature.Ultils.MediaPlayer.MediaInstance;
import com.ebot.MikoBot.Feature.Ultils.MediaPlayer.MediaManager;
import com.ebot.MikoBot.MainClass;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

import static com.ebot.MikoBot.BotInstance.MUSIC;
import static com.ebot.MikoBot.BotInstance.TTS;

public class MediaPlayback {
    static String MEDIA_PREFIX = "/";
    private TextChannel textChannel;
    private String messageId;
    private MessageReceivedEvent event;
    private MediaInstance mediaInstance;
    private String BOT_ID;

    public MediaPlayback(String BOT_ID){
        this.BOT_ID = BOT_ID;
    }

    /**
     * Start the media playback base on user request
     *
     * @param event User event which trigger this
     */
    public void start(MessageReceivedEvent event) {
        this.event = event;
        Member member = event.getMember();
        assert member != null;

        textChannel = event.getTextChannel();
        messageId = event.getMessageId();

        String[] message = event.getMessage().getContentDisplay().split("\n");

        MapMessageIDChannel.setCurrentMessageId(textChannel, event.getMessageId());
        if (event.getAuthor().isBot() && event.getAuthor().getJDA().getSelfUser().getId().equals(BOT_ID) && event.getMessage().getContentDisplay().startsWith(">>> ```")) {
            MapMessageIDChannel.setBotLastMessageId(textChannel, event.getMessageId());
        }

        Thread thread = new Thread(() -> {
            for (String s : message) {
                if (!s.startsWith(MEDIA_PREFIX)) {
                    continue;
                }
                String content = s.substring(1);

                String cmd = content.substring(0, content.contains(" ") ? content.indexOf(" ") : content.length());

                content = content.replaceFirst(cmd, "").replace(" ", "");

                VoiceChannel voiceChannel = Objects.requireNonNull(member.getVoiceState()).getChannel();

                if (voiceChannel != null) {
                    mediaInstance = MediaManager.connectTo(event.getGuild(), voiceChannel,MUSIC);
                    mediaInstance.getController().setTextChannel(textChannel);

                    try{
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
                                    react(":x:");
                                    return;
                                }
                            case "remove":

                                int i;
                                if (!content.equals("")) {
                                    if ((i = Integer.parseInt(content)) > 0)
                                        mediaInstance.getController().remove(i - 1);
                                    break;
                                } else {
                                    react(":x:");
                                    return;
                                }

                            case "setVol":
                                int vol;
                                if (!content.equals("")) {
                                    if ((vol = Integer.parseInt(content)) > 0){
                                        mediaInstance.getController().setVolume(vol);
                                    }
                                    break;
                                } else {
                                    react(":x:");
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
                                } catch (Exception ex) {
                                    System.out.println(ex.getMessage());
                                    react(":x:");
                                    return;
                                }
                                break;
//                            case "reboot_":
//                                MainClass.reboot(MUSIC);
//                                break;
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
                                    react(":x:");
                                    return;
                                }

                            default:
                                react(":x:");
                                return;
                        }
                        react(":ok_hand:");
                    }
                    catch (Exception ex){
                        System.out.println(ex.getMessage());
                        react(":boom:");
                        return;
                    }

                } else {
                    react(":x:");
                }

            }
        });
        thread.start();
    }

    private void react(String input) {
        if (!event.getAuthor().isBot()) {
            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(input)).queue();
        }
    }

}