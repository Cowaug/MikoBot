package com.ebot.MikoBot.NOGUI;

import com.ebot.MikoBot.BotInstance;

public class Console {
    private BotInstance botInstance = null;

    /**
     * Create the GUI
     * and add a safety closing mechanism
     */
    public Console(String[] args) {
        String token = null;
        String mode = null;
        String region = null;
        try {
            token = args[0];
            mode = args[1];
            region = args[2];
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

//        token = ""; // TOKEN GOES HERE
//        mode = "TTS"; // MODE GOES HERE


        botInstance = new BotInstance(token, mode,region);

    }

    public String getBotId() {
        return botInstance.getId();
    }

    public String getMode() {
        return botInstance.getMode();
    }

    public void shutDown() {
        try {
            botInstance.shutdown();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.exit(0);
    }
}
