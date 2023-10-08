package one.tain.emoji.model;


import one.tain.emoji.EmojiType;

import java.util.List;

public class Emoji {
    public final String emojiChar;
    public final String unicode;
    public final String description;
    public final List<String> aliases;
    public final List<String> group;
    public final List<String> subgroup;
    public final EmojiType type;
    public final boolean supportsFitzpatrick;
    public final boolean supportsGender;

    public Emoji(String emojiChar, String unicode, String description, List<String> aliases, List<String> group, List<String> subgroup, EmojiType type, boolean supportsFitzpatrick, boolean supportsGender) {
        this.emojiChar = emojiChar;
        this.unicode = unicode;
        this.description = description;
        this.aliases = aliases;
        this.group = group;
        this.subgroup = subgroup;
        this.type = type;
        this.supportsFitzpatrick = supportsFitzpatrick;
        this.supportsGender = supportsGender;
    }
}
