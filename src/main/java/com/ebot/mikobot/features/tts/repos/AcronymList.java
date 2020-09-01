package com.ebot.mikobot.features.tts.repos;

import com.ebot.mikobot.features.tts.model.Acronym;
import com.ebot.mikobot.ultils.JawMySQL;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class AcronymList {
    private static ArrayList<Acronym> words = null;

    /**
     * Replace pre-defined slang word in the string
     * with the formal word
     *
     * @param sentence Sentence contains slang
     * @return Sentence without slang
     */
    public static String replaceAcronym(String sentence) {
        if (words == null)
            load();
        sentence = " " + sentence + " ";
        String[] specialChar = {" ", ",", ".", "?"};
        for (Acronym w : words)
            for (String s : specialChar)
                sentence = sentence.replace(s + w.getSlang() + s, s + w.getFormal() + s);
        return sentence.substring(1, sentence.length() - 1);
    }

    /**
     * Define a new slang
     *
     * @param acronym Slang need to be defined
     * @param formal  Formal word corresponding to Slang
     */
    public static void addAcronym(String acronym, String formal) {
        if (words == null) load();
        words.removeIf(x -> x.getSlang().equals(acronym));
        words.add(new Acronym(acronym, formal));
        JawMySQL.addAcronym(acronym, formal);
    }

    /**
     * Remove a slang
     *
     * @param acronym Slang need to be removed
     */
    public static void removeAcronym(String acronym) {
        if (words == null) load();
        words.removeIf(x -> x.getSlang().equals(acronym));
        JawMySQL.removeAcronym(acronym);
    }

    /**
     * Load defined slang for use
     */
    private static void load() {
        if (words != null)
            words.clear();
        else words = new ArrayList<>();
        words.addAll(JawMySQL.loadAcronym());
    }

    /**
     * List all acronym to specific text channel
     * @param textChannel Text channel to list
     */
    public static void list(TextChannel textChannel) {
        if (words == null)
            load();
        StringBuilder output = new StringBuilder(">>> ");
        words.stream().sorted().forEach(acronym -> output.append(acronym.getSlang()).append("\t").append(acronym.getFormal()).append("\n"));
        textChannel.sendMessage(output).queue();
    }
}
