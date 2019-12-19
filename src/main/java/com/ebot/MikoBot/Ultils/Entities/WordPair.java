package com.ebot.MikoBot.Ultils.Entities;

import java.io.Serializable;

public class WordPair implements Serializable {
    private String formal;
    private String slang;

    /**
     * Create new word
     *
     * @param slang  Slang
     * @param formal Formal of the corresponding slang
     */
    public WordPair(String slang, String formal) {
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
