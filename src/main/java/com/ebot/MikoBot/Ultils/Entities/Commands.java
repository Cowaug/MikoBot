package com.ebot.MikoBot.Ultils.Entities;

public class Commands {
    public enum TTS {
        DOT(true,"."),
        LOCKME;
        public final boolean needInVoice;
        public final String value;

        TTS(boolean needInVoice) {
            this.needInVoice = needInVoice;
            this.value = this.toString();
        }

        TTS(boolean needInVoice, String value) {
            this.needInVoice = needInVoice;
            this.value = value.toUpperCase();
        }

        TTS() {
            this.needInVoice = false;
            this.value = this.toString();
        }

        TTS(String value) {
            this.needInVoice = false;
            this.value = value.toUpperCase();
        }
    }

    public enum MUSIC {
        PLAY(true),
        REMOVE(true),
        SETVOL(true),
        STOP(true),
        NEXT(true),
        LOOPONE(true),
        LOOPALL(true),
        LOOPOFF(true),
        CLEAR(true),
        PAUSE(true),
        RESUME(true),
        JOIN(true),
        QUEUE(true),
        PAGE(true),
        LEAVE,
        INFO;
        public final boolean needInVoice;

        MUSIC(boolean needInVoice) {
            this.needInVoice = needInVoice;
        }

        MUSIC() {
            this.needInVoice = false;
        }
    }
}
