package one.tain.emoji.model;


import java.util.List;

public class Emoji {
    public final String emojiChar;
    public final String emoji;
    public final String description;
    public final List<String> aliases;
    public final List<String> tags;
    public final boolean supportsFitzpatrick;
    public final boolean supportsGender;

    public Emoji(String emojiChar, String emoji, String description, List<String> aliases, List<String> tags, boolean supportsFitzpatrick, boolean supportsGender) {
        this.emojiChar = emojiChar;
        this.emoji = emoji;
        this.description = description;
        this.aliases = aliases;
        this.tags = tags;
        this.supportsFitzpatrick = supportsFitzpatrick;
        this.supportsGender = supportsGender;
    }


}
