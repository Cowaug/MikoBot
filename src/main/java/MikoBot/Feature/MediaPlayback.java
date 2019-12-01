package MikoBot.Feature;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vdurmont.emoji.EmojiParser;
import MikoBot.MediaManager;
import MikoBot.MediaPlayer.MediaInstance;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class MediaPlayback {
    public static String MEDIA_PREFIX="/";

    /**
     * Start the media playback base on user request
     *
     * @param event User event which trigger this
     */
    public void start(MessageReceivedEvent event) {
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        TextChannel textChannel = event.getTextChannel();
        String messageId = event.getMessageId();

        String[] message = event.getMessage().getContentDisplay().split("\n");

        for (String s : message) {
            String content = s.substring(1);

            String cmd = content.substring(0, content.contains(" ") ? content.indexOf(" ") : content.length());

            content = content.replaceFirst(cmd, "").replace(" ", "");

            VoiceChannel voiceChannel = Objects.requireNonNull(member.getVoiceState()).getChannel();
            if (voiceChannel != null) {
                MediaInstance mediaInstance = MediaManager.connectTo(event.getGuild(), voiceChannel);
                switch (cmd) {
                    case "play":
                        if (!content.equals("")) {
                            try {
                                int customIdx = Integer.parseInt(content);
                                mediaInstance.getController().jumpTo(customIdx);
                                break;
                            } catch (Exception ignored){
                                System.out.println("Not index");
                            }
                            mediaInstance.play(content, textChannel);
                            break;
                        } else {
                            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                            return;
                        }
                    case "remove":
                        try {
                            int i;
                            if (!content.equals("") && (i = Integer.parseInt(content)) >= 0) {
                                mediaInstance.getController().remove(i);
                                break;
                            } else {
                                textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                            return;
                        }
                    case "setVol":
                        try {
                            int vol;
                            if (!content.equals("") && (vol = Integer.parseInt(content)) > 0) {
                                mediaInstance.getController().setVolume(vol);
                                break;
                            } else {
                                textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
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
                        List<AudioTrack> queue = mediaInstance.getController().getQueue();
                        for (int i = 0; i < queue.size(); i++) {
                            AudioTrack audioTrack = queue.get(i);
                            textChannel.sendMessage(i + ". " + audioTrack.getInfo().title).queue();
                        }
                        break;
                    default:
                        textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                        return;
                }
                mediaInstance.getController().setTextChannel(textChannel);
                textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":ok_hand:")).queue();
            } else {
                textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
            }
        }
    }
}
