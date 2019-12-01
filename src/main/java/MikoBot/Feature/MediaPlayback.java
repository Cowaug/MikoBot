package MikoBot.Feature;

import MikoBot.MediaManager;
import MikoBot.MediaPlayer.MediaInstance;
import MikoBot.Run;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
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

        for (String s : message) {
            if (!s.startsWith(MEDIA_PREFIX)) continue;
            String content = s.substring(1);

            String cmd = content.substring(0, content.contains(" ") ? content.indexOf(" ") : content.length());

            content = content.replaceFirst(cmd, "").replace(" ", "");

            VoiceChannel voiceChannel = Objects.requireNonNull(member.getVoiceState()).getChannel();

            if (voiceChannel != null) {
                mediaInstance = MediaManager.connectTo(event.getGuild(), voiceChannel);
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
                mediaInstance.getController().setTextChannel(textChannel);
                react(":ok_hand:");
            } else {
                react(":x:");
            }
        }
    }

    private void react(String input) {
        if (!event.getAuthor().isBot())
            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(input)).queue();
        else if(event.getAuthor().getJDA().getSelfUser().getId().equals(Run.console.getBotId())){
            if(mediaInstance!=null) mediaInstance.getController().setMessageId(event.getMessageId());
        }
    }
}