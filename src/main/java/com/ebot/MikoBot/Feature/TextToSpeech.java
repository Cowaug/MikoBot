package com.ebot.MikoBot.Feature;

import com.ebot.MikoBot.BotInstance;
import com.ebot.MikoBot.MainClass;
import com.ebot.MikoBot.Ultils.BackupAndRestore;
import com.ebot.MikoBot.Ultils.Entities.WordPair;
import com.ebot.MikoBot.Ultils.MediaPlayer.MediaInstance;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static com.ebot.MikoBot.Feature.PlayingMusic.MEDIA_PREFIX;
import static com.ebot.MikoBot.MainClass.PROGRAM_PATH;
import static com.ebot.MikoBot.Ultils.TextChannelManager.react;

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
            autoTTS = BackupAndRestore.loadStringList("autoTTS.txt");
            autoTTSDelete = BackupAndRestore.loadStringList("autoTTSDelete.txt");
        } catch (Exception ignore) {
            if (autoTTS == null) autoTTS = new ArrayList<>();
            if (autoTTSDelete == null) autoTTSDelete = new ArrayList<>();
        }
    }

    /**
     * Start the TTS process
     *
     * @param event The message event that need to be spoken
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

        MediaInstance mediaInstance = botInstance.get(event);
        if (mediaInstance != null) {
            String cmd = getCmd(content);
            content = content.replaceFirst(cmd, "");
            switch (cmd) {
                case ",":
                    textChannel.deleteMessageById(messageId).queue();
                    GoogleTranslate(event, mediaInstance, content);
                    return;
                case ".":
                    GoogleTranslate(event, mediaInstance, content);
                    return;
                case "lockme":
                    if (!autoTTS.contains(memberId)) {
                        autoTTS.add(memberId);
                        BackupAndRestore.saveStringList(autoTTS, "autoTTS.txt");
                    }
                    break;
                case "unlockme":
                    autoTTS.remove(memberId);
                    BackupAndRestore.saveStringList(autoTTS, "autoTTS.txt");
                    break;
                case "add":
                    if (!content.equals("")) {
                        String[] str = {null, null};

                        str[0] = content.substring(0, content.indexOf(" "));
                        str[1] = content.substring(content.indexOf(" ") + 1);

                        if (str[0].equals("") || str[1].equals("")) return;

                        Acronym.addAcronym(str[0], str[1]);
                        break;

                    } else {
                        react(event, ":x:");
                        return;
                    }
                case "remove": {
                    if (!content.equals("")) {
                        Acronym.removeAcronym(content.replace(" ", ""));
                        break;
                    } else {
                        react(event, ":x:");
                        return;
                    }
                }
                case "delete":
                    if (!autoTTSDelete.contains(memberId)) autoTTSDelete.add(memberId);
                    BackupAndRestore.saveStringList(autoTTSDelete, "autoTTSDelete.txt");
                    break;
                case "keep":
                    autoTTSDelete.remove(memberId);
                    BackupAndRestore.saveStringList(autoTTSDelete, "autoTTSDelete.txt");
                    break;
                case "list":
                    Acronym.list(textChannel);
                    break;
//                    case "reboot_":
//                        MainClass.reboot(TTS);
//                        break;
//                case "info":
//                    TextChannelManager.updateMessage(botInstance, event, TextChannelManager.getInfoTTS());
//                    break;
                case "skip":
                    mediaInstance.getController().nextTrack(false);
                    break;
                case "leave":
                    mediaInstance.disconnect();
                    break;
                default:
                    react(event, ":x:");
                    return;
            }
            react(event, ":ok_hand:");
        } else {
            if (!autoIgnore) react(event, ":x:");
        }
    }

    /**
     * Speak the text using the server's player
     * created previously
     *
     * @param mediaInstance Server's player
     * @param text          Text user wants to speak
     */
    private void GoogleTranslate(MessageReceivedEvent event, MediaInstance mediaInstance, String text) {
        text = Acronym.replaceAcronym(text);
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
            mediaInstance.reconnect(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel());
            mediaInstance.play(url + URLEncoder.encode(text.replace("@", "@ "), "UTF-8"), null);
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
}

class Acronym {
    private static ArrayList<WordPair> words = null;

    /**
     * Replace pre-defined slang word in the string
     * with the formal word
     *
     * @param sentence Sentence contains slang
     * @return Sentence without slang
     */
    static String replaceAcronym(String sentence) {
        if (words == null) {
            words = new ArrayList<>();
            load();
        }
        sentence = " " + sentence + " ";
        for (WordPair w : words) {
            sentence = sentence.replace(" " + w.getSlang() + " ", " " + w.getFormal() + " ");
        }
        return sentence.substring(1, sentence.length() - 1);
    }

    /**
     * Define a new slang
     *
     * @param acronym  Slang need to be defined
     * @param formal Formal word corresponding to Slang
     */
    static void addAcronym(String acronym, String formal) {
        for (WordPair w : words)
            if (w.getSlang().equals(acronym)) return;
        words.add(new WordPair(acronym, formal));
        save();
    }

    /**
     * Remove a slang
     *
     * @param acronym Slang need to be removed
     */
    static void removeAcronym(String acronym) {
        for (int i = 0; i < words.size(); i++) {
            WordPair w = words.get(i);
            if (w.getSlang().equals(acronym)) {
                words.remove(i);
                break;
            }
        }
        save();
    }

    /**
     * Save defined slang to disk
     */
    private static void save() {
        BackupAndRestore.saveAcronym(words);
    }

    /**
     * Load defined slang for use
     */
    private static void load() {
        words.clear();
        words.addAll(BackupAndRestore.loadAcronym());
    }

    public static void list(TextChannel textChannel) {
        if (words == null) {
            words = new ArrayList<>();
            load();
        }
        StringBuilder output = new StringBuilder(">>> ");
        for (WordPair wordPair : words) {
            output.append(wordPair.getSlang()).append("\t").append(wordPair.getFormal()).append("\n");
        }
        textChannel.sendMessage(output).queue();
    }
}

