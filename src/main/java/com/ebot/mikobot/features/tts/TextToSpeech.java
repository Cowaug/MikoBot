package com.ebot.mikobot.features.tts;

import com.ebot.mikobot.bots.models.BotInstance;
import com.ebot.mikobot.features.tts.repos.AcronymList;
import com.ebot.mikobot.features.tts.repos.VoiceReferenceList;
import com.ebot.mikobot.ultils.JawMySQL;
import com.ebot.mikobot.features.mediaplayer.model.MediaInstance;
import com.ebot.mikobot.ultils.TextChannelManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.ebot.mikobot.features.mediaplayer.PlayingMusic.MEDIA_PREFIX;
import static com.ebot.mikobot.ultils.TextChannelManager.sentReact;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class TextToSpeech {
    private static final String EN = "en";
    private static final String VN = "vi";
    private static final String JP = "ja";

    private ArrayList<String> autoTTS;
    private ArrayList<String> autoTTSDelete;
    private BotInstance botInstance;

    /**
     * Load the list of user whom prefer
     * automatic TTS (and delete message afterward)
     */
    public TextToSpeech(BotInstance botInstance) {
        this.botInstance = botInstance;
        try {
            autoTTS = JawMySQL.loadTable("autoTTS");
        } catch (Exception ignore) {
            autoTTS = new ArrayList<>();
        }

        try {
            autoTTSDelete = JawMySQL.loadTable("autoTTSDelete");
        } catch (Exception ignore) {
            autoTTSDelete = new ArrayList<>();
        }
    }

    /**
     * Start the TTS process
     *
     * @param event The message event that need to be spoken
     */
    public void execute(MessageReceivedEvent event) {
        try {
            Member member = event.getMember();
            assert member != null;
            String memberId = member.getId();
            VoiceChannel voiceChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
            TextChannel textChannel = event.getTextChannel();
            String messageId = event.getMessageId();
            String content = event.getMessage().getContentDisplay();

            boolean autoIgnore;

            final String TTS_PREFIX = ".";
            final String[] IGNORE_KEYWORD = {MEDIA_PREFIX, "`", "http://", "https://"};

            if (content.startsWith(TTS_PREFIX)) {
                autoIgnore = false;
                content = content.replaceFirst(TTS_PREFIX, "");
            } else if (Arrays.stream(IGNORE_KEYWORD).noneMatch(content::startsWith) && autoTTS.contains(memberId)) {
                autoIgnore = true;
                if (autoTTSDelete.contains(memberId))
                    content = "," + content;
                else content = "." + content;
            } else return;

            String cmd = getCmd(content);
            content = content.replaceFirst(cmd, "");
            String[] needInVoiceCommand = ". , skip voiceDemo".split(" ");

            MediaInstance mediaInstance = botInstance.getMediaInstance(event);

            if (voiceChannel != null || !Arrays.asList(needInVoiceCommand).contains(cmd)) {
                switch (cmd) {
                    case ",":
                        textChannel.deleteMessageById(messageId).queue();
                    case ".":
                        playTTS(voiceChannel, mediaInstance, GoogleTranslate(memberId, messageId, content));
                        return;
                    case "lockme":
                        if (!autoTTS.contains(memberId)) {
                            autoTTS.add(memberId);
                            JawMySQL.addToTable("autoTTS", memberId);
                        }
                        break;
                    case "unlockme":
                        autoTTS.remove(memberId);
                        JawMySQL.removeFromTable("autoTTS", memberId);
                        break;
                    case "add":
                        content = content.trim();
                        if (!content.equals("")) {
                            String[] str = {null, null};

                            str[0] = content.substring(0, content.indexOf(" "));
                            str[1] = content.replaceFirst(str[0], "");

                            if (str[0].equals("") || str[1].equals("")) {
                                sentReact(event, ":x:");
                                return;
                            }

                            AcronymList.addAcronym(str[0], str[1]);
                            break;

                        } else {
                            sentReact(event, ":x:");
                            return;
                        }
                    case "remove": {
                        if (!content.equals("")) {
                            AcronymList.removeAcronym(content.trim());
                            break;
                        } else {
                            sentReact(event, ":x:");
                            return;
                        }
                    }
                    case "delete":
                        if (!autoTTSDelete.contains(memberId)) autoTTSDelete.add(memberId);
                        JawMySQL.addToTable("autoTTSDelete", memberId);
                        break;
                    case "keep":
                        autoTTSDelete.remove(memberId);
                        JawMySQL.removeFromTable("autoTTSDelete", memberId);
                        break;
                    case "setVoice":
                        short voiceRef = 1;
                        try {
                            voiceRef = Short.parseShort(content.trim());
                        } catch (Exception ex) {
                            System.out.print(ex.getMessage());
                        }
                        VoiceReferenceList.modifyUserRef(memberId, voiceRef);
                        break;
                    case "list":
                        AcronymList.list(textChannel);
                        break;
                    case "info":
                        TextChannelManager.updateMessage(botInstance, event, TextChannelManager.getInfoTTS());
                        break;
                    case "voiceDemo":
                        String demoText = "voice thứ ";
                        int i;
                        try {
                            i = Integer.parseInt(content.trim());
                            int tmp = i > 4 ? 0 : i;
                            tmp = Math.max(1, tmp);
                            playTTS(voiceChannel, mediaInstance, GoogleTranslate("xxxxxxxxxxxxxxxxx" + tmp, messageId, demoText + i));
                        } catch (Exception ex) {
                            System.out.print(ex.getMessage());
                            for (i = 1; i <= 4; i++) {
                                playTTS(voiceChannel, mediaInstance, GoogleTranslate("xxxxxxxxxxxxxxxxx" + i, messageId, demoText + i));
                            }
                        }
                        break;
                    case "skip":
                        mediaInstance.getController().nextTrack(false);
                        break;
                    case "leave":
                        mediaInstance.disconnect();
                        break;
                    default:
                        sentReact(event, ":question:");
                        return;
                }
                sentReact(event, ":ok_hand:");
            } else {
                if (!autoIgnore) {
                    sentReact(event, ":headphones:");
                    sentReact(event, ":exclamation:");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sentReact(event, ":boom:");
        }
    }

    /**
     * Speak the text using the server's player
     * created previously
     *
     * @param text Text user wants to speak
     */
    private String GoogleTranslate(String memberId, String messageId, String text) {
        text = AcronymList.replaceAcronym(text);
        String language = VN;
        if (text.startsWith(EN + " ")) {
            text = text.replaceFirst(EN + " ", "");
            language = EN;
        } else if (text.startsWith(VN + " ")) {
            text = text.replaceFirst(VN + " ", "");
            language = VN;
        } else if (text.startsWith(JP + " ")) {
            text = text.replaceFirst(JP + " ", "");
            language = JP;
        }

        String url = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=" + language + "&q=";
        try {
            return getAudioPath(url + URLEncoder.encode(text.replace("@", "@ "), "UTF-8"), memberId, messageId, VoiceReferenceList.getVoiceRef(memberId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void playTTS(VoiceChannel voiceChannel, MediaInstance mediaInstance, String url) {
        if (url == null) return;
        mediaInstance.reconnect(voiceChannel);
        mediaInstance.play(url, null);
    }

    /**
     * Get the command from user chat
     *
     * @param messageContent User chat text
     * @return Command
     */
    private String getCmd(String messageContent) {
        switch (messageContent.substring(0, 1)) {
            case ",":
                return ",";
            case ".":
                return ".";
            default:
                return messageContent.substring(0, messageContent.contains(" ") ? messageContent.indexOf(" ") : messageContent.length());
        }
    }

    /**
     * Get modified TTS Audio File Path
     * @param url Google TTS url of the input text
     * @param memberId Member Id (User Id in Discord)
     * @param messageId Message's Id
     * @param voiceRef Voice reference of corresponding user
     * @return TTS Audio File PAth
     * @throws IOException IOEx
     * @throws UnsupportedAudioFileException Unsupported Format
     */
    private String getAudioPath(String url, String memberId, String messageId, short voiceRef) throws IOException, UnsupportedAudioFileException {
        System.setProperty("http.agent", "Chrome");
        InputStream in = new URL(url).openStream();
        Files.copy(in, Paths.get(memberId + messageId + ".mp3"), StandardCopyOption.REPLACE_EXISTING);
        final File file = new File(memberId + messageId + ".mp3");
        final AudioInputStream inputStream = getAudioInputStream(file);
        AudioSystem.write(getAudioInputStream(getOutFormat(inputStream.getFormat(), voiceRef), inputStream), AudioFileFormat.Type.WAVE, new File(memberId + messageId + ".wav"));
        new Thread(() -> {
            try {
                Thread.sleep(2 * 60 * 1000);
            } catch (InterruptedException e) {
                System.out.print(e.getMessage());
            }
            try {
                Files.delete(Paths.get(memberId + messageId + ".mp3"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Files.delete(Paths.get(memberId + messageId + ".wav"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return memberId + messageId + ".wav";
    }

    /**
     * Audio Format base on user's Voice Reference
     * @param inFormat Original Format
     * @param voiceRef User's Voice Reference
     * @return Modified Audio Format
     */
    private static AudioFormat getOutFormat(AudioFormat inFormat, int voiceRef) {
        int add;
        switch (voiceRef) {
            default:
                add = 0;
                break;
            case 2:
                add = 3000;
                break;
            case 3:
                add = 6000;
                break;
            case 4:
                add = -3000;
                break;
        }
        return new AudioFormat(PCM_SIGNED, inFormat.getSampleRate() + add, 16, inFormat.getChannels(), inFormat.getFrameSize(), inFormat.getFrameRate(),
                inFormat.isBigEndian());
    }
}

