package com.ebot.mikobot.bots.models;

import com.ebot.mikobot.features.mediaplayer.model.MediaInstance;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class GuildInstance {
    private Guild guild;
    private MediaInstance mediaInstance = null;
    private TextChannel lastTextChannel;
    private String lastBotsMessageId = null;
    private boolean isLastSendByBot = false;

    /**
     * Create instance of Guild (Server in Discord)
     * @param event
     * @param jda
     */
    GuildInstance(MessageReceivedEvent event, JDA jda) {
        this.guild = event.getGuild();
        update(event, jda);
    }

    Guild getGuild() {
        return guild;
    }

    MediaInstance getMediaInstance() {
        return mediaInstance;
    }

    /**
     * Save Bot's Last Message Id + Last Text Channel user issue command
     * @param event
     * @param jda
     */
    void update(MessageReceivedEvent event, JDA jda) {
        if (event.getAuthor().isBot() &&
                event.getMessage().getContentRaw().startsWith(">>> ") &&
                event.getAuthor().getJDA().getSelfUser().getIdLong() == jda.getSelfUser().getIdLong()) {
            this.lastBotsMessageId = event.getMessageId();
            this.lastTextChannel = event.getTextChannel();
            isLastSendByBot = true;
        } else isLastSendByBot = false;
    }

    void setMediaInstance(MediaInstance mediaInstance) {
        this.mediaInstance = mediaInstance;
    }

    boolean isLastSendByBot() {
        return isLastSendByBot;
    }

    String getBotsLastMessageId() {
        return lastBotsMessageId;
    }

    TextChannel getLastTextChannel() {
        return lastTextChannel;
    }
}
