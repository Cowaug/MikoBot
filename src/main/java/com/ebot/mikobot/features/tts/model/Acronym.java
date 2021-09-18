package com.ebot.mikobot.features.tts.model;

import java.io.Serializable;

public class Acronym implements Serializable {
    private final String formal;
    private final String slang;

    /**
     * Create new word
     *
     * @param slang  Slang
     * @param formal Formal of the corresponding slang
     */
    public Acronym(String slang, String formal) {
        this.slang = slang;
        this.formal = formal;
    }

    /**
     * Get the formal word
     *
     * @return Formal word
     */
    public String getFormal() {
        return formal;
    }

    /**
     * Get the slang
     *
     * @return Slang
     */
    public String getSlang() {
        return slang;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
