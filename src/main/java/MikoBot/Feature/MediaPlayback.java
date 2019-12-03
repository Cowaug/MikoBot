package MikoBot.Feature;

import MikoBot.Feature.Ultils.MediaPlayer.MediaManager;
import MikoBot.Feature.Ultils.MediaPlayer.MapMessageIDChannel;
import MikoBot.Feature.Ultils.MediaPlayer.MediaInstance;
import MikoBot.Run;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class MediaPlayback {
    public static String MEDIA_PREFIX = "/";
    private TextChannel textChannel;
    private String messageId;
    private MessageReceivedEvent event;
    private MediaInstance mediaInstance;

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

        MapMessageIDChannel.setCurrentMessageId(textChannel,event.getMessageId());
        if (event.getAuthor().isBot() && event.getAuthor().getJDA().getSelfUser().getId().equals(Run.console.getBotId()) && event.getMessage().getContentDisplay().startsWith("```")) {
            MapMessageIDChannel.setBotLastMessageId(textChannel,event.getMessageId());
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (String s : message) {
                    if (!s.startsWith(MEDIA_PREFIX)) {
                        react(":x:");
                        continue;
                    }
                    String content = s.substring(1);

                    String cmd = content.substring(0, content.contains(" ") ? content.indexOf(" ") : content.length());

                    content = content.replaceFirst(cmd, "").replace(" ", "");

                    VoiceChannel voiceChannel = Objects.requireNonNull(member.getVoiceState()).getChannel();

                    if (voiceChannel != null) {
                        mediaInstance = MediaManager.connectTo(event.getGuild(), voiceChannel);
                        mediaInstance.getController().setTextChannel(textChannel);

                        switch (cmd) {
                            case "play":
                                if (!content.equals("")) {
                                    try {
                                        int customIdx = Integer.parseInt(content);
                                        mediaInstance.getController().jumpTo(customIdx);
                                        break;
                                    } catch (Exception ignored) {
                                        System.out.println("Not index");
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
                                try {
                                    int i;
                                    if (!content.equals("") && (i = Integer.parseInt(content)) >= 0) {
                                        mediaInstance.getController().remove(i);
                                        break;
                                    } else {
                                        react(":x:");
                                        return;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    react(":x:");
                                    return;
                                }
                            case "setVol":
                                try {
                                    int vol;
                                    if (!content.equals("") && (vol = Integer.parseInt(content)) > 0) {
                                        mediaInstance.getController().setVolume(vol);
                                        break;
                                    } else {
                                        react(":x:");
                                        return;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    react(":x:");
                                    return;
                                }
                            case "stop":
                                mediaInstance.getController().stop();
                                break;
                            case "next":
                                mediaInstance.getController().nextTrack();
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
                            case "queue":
                                mediaInstance.getController().getQueue();
                                break;
                            default:
                                react(":x:");
                                return;
                        }
                        react(":ok_hand:");
                    } else {
                        react(":x:");
                    }

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