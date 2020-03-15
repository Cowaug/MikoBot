package com.ebot.MikoBot.Ultils.Entities;

public class Commands {
    public enum TTS {

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
