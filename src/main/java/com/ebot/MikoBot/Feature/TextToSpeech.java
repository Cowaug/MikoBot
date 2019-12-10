package com.ebot.MikoBot.Feature;

import com.ebot.MikoBot.Feature.Ultils.AutoReplaceWords.Slang;
import com.ebot.MikoBot.Feature.Ultils.MediaPlayer.MediaInstance;
import com.ebot.MikoBot.Feature.Ultils.MediaPlayer.MediaManager;
import com.ebot.MikoBot.MainClass;
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import static com.ebot.MikoBot.Feature.MediaPlayback.MEDIA_PREFIX;

public class TextToSpeech {
    private static final String EN = "en";
    private static final String VN = "vi";
    private static final String JP = "ja";

    private ArrayList<String> autoTTS;
    private ArrayList<String> autoTTSDelete;

    /**
     * Load the list of user whom prefer
     * automatic TTS (and delete message afterward)
     */
    public TextToSpeech() {
        try {
            autoTTS = load("autoTTS.txt");
            autoTTSDelete = load("autoTTSDelete.txt");
        } catch (Exception ignore) {
            if (autoTTS == null) autoTTS = new ArrayList<>();
            if (autoTTSDelete == null) autoTTSDelete = new ArrayList<>();
        }
    }

    /**
     * Start the TTS process
     *
     * @param event  The message event that need to be spoken
     */
    public void start(MessageReceivedEvent event) {
        Member member = event.getMember();
        assert member != null;
        String memberId = member.getId();
        TextChannel textChannel = event.getTextChannel();
        String messageId = event.getMessageId();
        String content = event.getMessage().getContentDisplay();

        boolean autoIgnore;

        String TTS_PREFIX = ".";
        String IGNORE_PREFIX = "`";

        if (content.startsWith(TTS_PREFIX)) {
            autoIgnore = false;
            content = content.replaceFirst(TTS_PREFIX, "");
        } else if (!content.startsWith(MEDIA_PREFIX) &&
                !content.startsWith(IGNORE_PREFIX) &&
                autoTTS.contains(memberId)) {
            autoIgnore = true;
            if (autoTTSDelete.contains(memberId))
                content = "," + content;
            else content = "." + content;
        } else return;

        VoiceChannel voiceChannel = Objects.requireNonNull(member.getVoiceState()).getChannel();
        if (voiceChannel != null) {
            String cmd = getCmd(content);
            content = content.replaceFirst(cmd, "");
            MediaInstance mediaInstance = MediaManager.connectTo(event.getGuild(), voiceChannel);
            switch (cmd) {
                case ",":
                    textChannel.deleteMessageById(messageId).queue();
                    GoogleTranslate(mediaInstance, content);
                    return;
                case ".":
                    GoogleTranslate(mediaInstance, content);
                    return;
                case "lockme":
                    if (!autoTTS.contains(memberId)) autoTTS.add(memberId);
                    save(autoTTS, "autoTTS.txt");
                    break;
                case "unlockme":
                    autoTTS.remove(memberId);
                    save(autoTTS, "autoTTS.txt");
                    break;
                case "add":
                    if (!content.equals("")) {
                        String[] str = {null, null};
                        try {
                            str[0] = content.substring(0, content.indexOf(" "));
                            str[1] = content.substring(content.indexOf(" "));

                            assert !str[0].equals("");
                            assert !str[1].equals("");

                            Slang.addSlang(str[0],str[1]);
                            break;
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                    }
                    break;
                case "remove":{
                    if (!content.equals("")) {
                        Slang.removeSlang(content);
                    } else {
                        textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                    }
                    break;
                }
                case "delete":
                    if (!autoTTSDelete.contains(memberId)) autoTTSDelete.add(memberId);
                    save(autoTTSDelete, "autoTTSDelete.txt");
                    break;
                case "keep":
                    autoTTSDelete.remove(memberId);
                    save(autoTTSDelete, "autoTTSDelete.txt");
                    break;
                case "shutdown_":
                    MainClass.console.shutDown();
                    break;
                case "skip":
                    MediaManager.connectTo(event.getGuild(), voiceChannel).getController().nextTrack(false);
                    break;
                case "leave":
                    mediaInstance.disconnect();
                    break;
                default:
                    textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
                    return;
                //HelpAndConfig.printWarning(textChannel, member, "Unknown command");
            }
            textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":ok_hand:")).queue();
        } else {
            if (!autoIgnore) textChannel.addReactionById(messageId, EmojiParser.parseToUnicode(":x:")).queue();
        }
    }

    /**
     * Speak the text using the server's player
     * created previously
     *
     * @param mediaInstance Server's player
     * @param text          Text user wants to speak
     */
    private void GoogleTranslate(MediaInstance mediaInstance, String text) {
        text = Slang.makeFormal(text);
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
            mediaInstance.play(url + URLEncoder.encode(text, "UTF-8"), null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
     * Save file to disk
     *
     * @param arrayList Array List of String need to save
     * @param filename  Filename on the disk
     */
    private void save(ArrayList<String> arrayList, String filename) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new FileWriter(MainClass.PROGRAM_PATH + "/" + filename);
            bufferedWriter = new BufferedWriter(writer);
            for (String s : arrayList) {
                bufferedWriter.write(s + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                assert bufferedWriter != null;
                bufferedWriter.close();
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get the file content and load as Array List of String
     *
     * @param filename filename on the disk
     * @return Array List of String
     */
    private ArrayList<String> load(String filename) {
        ArrayList<String> arrayList = new ArrayList<>();

        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new FileReader(MainClass.PROGRAM_PATH + "/" + filename);
            bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                arrayList.add(line);
            }
            return arrayList;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return arrayList;
    }
}
