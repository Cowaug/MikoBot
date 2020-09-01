package com.ebot.mikobot.features.tts.repos;

import com.ebot.mikobot.ultils.JawMySQL;
import com.ebot.mikobot.features.tts.model.VoiceReference;

import java.util.ArrayList;

public class VoiceReferenceList {
    private static ArrayList<VoiceReference> voiceRefs = null;

    /**
     * Change user reference
     * @param userId User Id
     * @param voiceRef Voice reference (1 - 4)
     */
    public static void modifyUserRef(String userId, short voiceRef) {
        if (voiceRefs == null) load();
        voiceRefs.removeIf(x -> x.getUserId().equals(userId));
        voiceRefs.add(new VoiceReference(userId, voiceRef));
        JawMySQL.modifyUserReference(userId, voiceRef);
    }

    /**
     * Load all user voice reference
     */
    private static void load() {
        if (voiceRefs != null)
            voiceRefs.clear();
        else voiceRefs = new ArrayList<>();
        voiceRefs.addAll(JawMySQL.loadUserReference());
    }

    /**
     * Get voice reference of specific user
     * @param userId User Id
     * @return Voice reference number
     */
    public static short getVoiceRef(String userId) {
        if (voiceRefs == null) load();
        for (VoiceReference x : voiceRefs) {
            if (x.getUserId().equals(userId)) {
                return x.getVoiceRef();
            }
        }
        modifyUserRef(userId, (short) 1);
        return 1;
    }
}
