package com.ebot.mikobot.features.tts.model;

public class VoiceReference {
    private final String userId;
    private final short voiceRef;

    /**
     * User voice reference
     * @param userId User Id
     * @param voiceRef Voice reference (1 - 4)
     */
    public VoiceReference(String userId, short voiceRef){
        this.userId = userId;
        if (voiceRef < 1 || voiceRef > 5){
            voiceRef = 1;
        }

        this.voiceRef = voiceRef;
    }

    public String getUserId() {
        return userId;
    }

    public short getVoiceRef() {
        return voiceRef;
    }
}
