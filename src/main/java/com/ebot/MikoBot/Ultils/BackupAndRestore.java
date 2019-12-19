package com.ebot.MikoBot.Ultils;

import com.ebot.MikoBot.MainClass;
import com.ebot.MikoBot.Ultils.Entities.WordPair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.ebot.MikoBot.MainClass.PROGRAM_PATH;

/**
 * Add functionality to reboot the bot without clearing user data
 */
public class BackupAndRestore {
    private static final String SPLITTER = " <>\\<> ";

    public static void saveAcronym(ArrayList<WordPair> words) {
        OutputStreamWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(PROGRAM_PATH + "/acronym-spoiler.txt"), StandardCharsets.UTF_8);
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

    public static ArrayList<WordPair> loadAcronym(){
        ArrayList<WordPair> words = new ArrayList<>();
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(PROGRAM_PATH + "/acronym-spoiler.txt"), StandardCharsets.UTF_8);

            bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String substring = line.substring(line.indexOf(SPLITTER) + SPLITTER.length());
                words.add(new WordPair(line.substring(0, line.indexOf(SPLITTER)), substring)); }

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
        return words;
    }

    public static ArrayList<String> loadStringList(String filename){
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

    public static void saveStringList(ArrayList<String> strings, String filename) {
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new FileWriter(MainClass.PROGRAM_PATH + "/" + filename);
            bufferedWriter = new BufferedWriter(writer);
            for (String s : strings) {
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
}

class SyncWithGit{
    public static void pull() throws IOException, InterruptedException {
        String command = "ping -c 3 www.google.com;ping -c 3 www.google.com";
        Process proc = Runtime.getRuntime().exec(command);
//        BufferedReader reader =
//                new BufferedReader(new InputStreamReader(proc.getInputStream()));
//        String line = "";
//        while((line = reader.readLine()) != null) {
//            System.out.print(line + "\n");
//        }
        proc.waitFor();

    }
}
