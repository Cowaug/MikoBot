package MikoBot.Feature.Ultils.AutoReplaceWords;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static MikoBot.Run.PROGRAM_PATH;

public class Slang {
    private static final String SPLITTER = " <>\\<> ";
    private static ArrayList<WordPair> words = null;

    /**
     * Replace pre-defined slang word in the string
     * with the formal word
     *
     * @param sentence Sentence contains slang
     * @return Sentence without slang
     */
    public static String makeFormal(String sentence) {
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
     * @param slang  Slang need to be defined
     * @param formal Formal word corresponding to Slang
     */
    public static void addSlang(String slang, String formal) {
        for (WordPair w : words)
            if (w.getSlang().equals(slang)) return;
        words.add(new WordPair(slang, formal));
        save();
    }

    /**
     * Remove a slang
     *
     * @param slang Slang need to be removed
     */
    public static void removeSlang(String slang) {
        for (WordPair w : words) {
            if (w.getSlang().equals(slang)) words.remove(w);
            break;
        }
        save();
    }

    /**
     * Save defined slang to disk
     */
    private static void save() {
        OutputStreamWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(PROGRAM_PATH + "/slang-spoiler.txt"), StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(writer);
            for (WordPair w : words) {
                bufferedWriter.write(w.getSlang() + SPLITTER + w.getFormal() + "\n");
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
     * Load defined slang for use
     */
    private static void load() {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(PROGRAM_PATH + "/slang-spoiler.txt"), StandardCharsets.UTF_8);

            bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String substring = line.substring(line.indexOf(SPLITTER) + SPLITTER.length());
                words.add(new WordPair(line.substring(0, line.indexOf(SPLITTER)), substring));
                System.out.println("Added: " + line.substring(0, line.indexOf(SPLITTER)) + " as " + substring);
            }

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
    }
}

class WordPair implements Serializable {
    private String formal;
    private String slang;

    /**
     * Create new word
     *
     * @param slang  Slang
     * @param formal Formal of the corresponding slang
     */
    WordPair(String slang, String formal) {
        this.slang = slang;
        this.formal = formal;
    }

    /**
     * Get the formal word
     *
     * @return Formal word
     */
    String getFormal() {
        return formal;
    }

    /**
     * Get the slang
     *
     * @return Slang
     */
    String getSlang() {
        return slang;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
