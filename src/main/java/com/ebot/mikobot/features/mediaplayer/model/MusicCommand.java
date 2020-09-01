package com.ebot.mikobot.features.mediaplayer.model;

public enum MusicCommand {
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

    MusicCommand(boolean needInVoice) {
        this.needInVoice = needInVoice;
    }

    MusicCommand() {
        this.needInVoice = false;
    }
}
