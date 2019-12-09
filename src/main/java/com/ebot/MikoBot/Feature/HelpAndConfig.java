package com.ebot.MikoBot.Feature;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class HelpAndConfig {
    public static  void printTTSHelp(MessageChannel messageChannel, Member member) {
        messageChannel.sendMessage(
                "*Here is the wiki " + member.getAsMention() + ":\n"
                        + "``.join`` to summon the bot\n"
                        + "``./`` to talk with your name\n"
                        + "``..`` to talk without your name\n"
                        + "``.stop`` to stop current speech\n"
                        + "``.leave`` to disconnect the bot\n"
                        + "``.lockme`` / ``.unlockme`` to make the bot says / ignores whatever you type\n"
                        + "``.delete`` / ``.keep`` to make the bot delete / keep the messages after spoken\n"
                        + "``.lockchannel`` / ``.unlockchannel`` to make the bot says / ignores whatever typed in the channel\n"
                        + "``.add keyword = fullword`` / ``.remove keyword`` to add / remove slang\n"
                        + "``.prefix character`` to change the bot prefix\n"
                        + "``.ban @someone`` / ``.unban @someone`` to ban / unban user from using the bot*"
                        + "``` Use '*' prefix to force the bot ignore and not remvoe your message if you have .lockme and .delete activated.```").queue();
    }

    public static  void printMediaHelp(MessageChannel messageChannel, Member member) {
        messageChannel.sendMessage(
                "*Here is the wiki " + member.getAsMention() + ":\n"
                        + "``.join`` to summon the bot\n"
                        + "``./`` to talk with your name\n"
                        + "``..`` to talk without your name\n"
                        + "``.stop`` to stop current speech\n"
                        + "``.leave`` to disconnect the bot\n"
                        + "``.lockme`` / ``.unlockme`` to make the bot says / ignores whatever you type\n"
                        + "``.delete`` / ``.keep`` to make the bot delete / keep the messages after spoken\n"
                        + "``.lockchannel`` / ``.unlockchannel`` to make the bot says / ignores whatever typed in the channel\n"
                        + "``.add keyword = fullword`` / ``.remove keyword`` to add / remove slang\n"
                        + "``.prefix character`` to change the bot prefix\n"
                        + "``.ban @someone`` / ``.unban @someone`` to ban / unban user from using the bot*"
                        + "``` Use '*' prefix to force the bot ignore and not remvoe your message if you have .lockme and .delete activated.```").queue();
    }

    public static void printWarning(MessageChannel messageChannel, Member member, String warning) {
        messageChannel.sendMessage(warning + " " + member.getAsMention()).queue();
    }
}
