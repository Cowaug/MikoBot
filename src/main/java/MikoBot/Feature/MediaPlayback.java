package MikoBot.Feature;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.vdurmont.emoji.EmojiParser;
import MikoBot.MediaManager;
import MikoBot.MediaPlayer.MediaPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;

public class MediaPlayback {
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
                MediaPlayer mediaPlayer = MediaManager.connectTo(event.getGuild(), voiceChannel);
                switch (cmd) {
                    case "play":
                        if (!content.equals("")) {
                            try {
                                int customIdx = Integer.parseInt(content);
                                mediaPlayer.getController().jumpTo(customIdx);
                                break;
                            } catch (Exception ignored){
                                System.out.println("Not index");
                            }
                            mediaPlayer.play(content, textChannel);
                            break;
                        } else {
                            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                            return;
                        }
                    case "remove":
                        try {
                            int i;
                            if (!content.equals("") && (i = Integer.parseInt(content)) >= 0) {
                                mediaPlayer.getController().remove(i);
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
                                mediaPlayer.getController().setVolume(vol);
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
                        mediaPlayer.getController().stop();
                        break;
                    case "next":
                        mediaPlayer.getController().nextTrack();
                        break;
                    case "loopOne":
                        mediaPlayer.getController().setLoopOne();
                        break;
                    case "loopAll":
                        mediaPlayer.getController().setLoopAll();
                        break;
                    case "loopOff":
                        mediaPlayer.getController().setLoopOff();
                        break;
                    case "clear":
                        mediaPlayer.getController().clear();
                        break;
                    case "queue":
                        List<AudioTrack> queue = mediaPlayer.getController().getQueue();
                        for (int i = 0; i < queue.size(); i++) {
                            AudioTrack audioTrack = queue.get(i);
                            textChannel.sendMessage(i + ". " + audioTrack.getInfo().title).queue();
                        }
                        break;
                    default:
                        textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                        return;
                }
                mediaPlayer.getController().setTextChannel(textChannel);
                textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":ok_hand:")).queue();
            } else {
                textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
            }
        }
    }
}
