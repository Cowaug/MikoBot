package com.ebot.mikobot.features.tts.model;

public enum TtsCommand {
    DOT(true,"."),
    LOCKME;
    public final boolean needInVoice;
    public final String value;

    TtsCommand(boolean needInVoice) {
        this.needInVoice = needInVoice;
        this.value = this.toString();
    }

    TtsCommand(boolean needInVoice, String value) {
        this.needInVoice = needInVoice;
        this.value = value.toUpperCase();
    }

    TtsCommand() {
        this.needInVoice = false;
        this.value = this.toString();
    }

    TtsCommand(String value) {
        this.needInVoice = false;
        this.value = value.toUpperCase();
    }
}
