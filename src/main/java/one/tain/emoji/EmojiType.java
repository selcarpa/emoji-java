package one.tain.emoji;

public enum EmojiType {
    /**
     * a fully-qualified emoji (see ED-18 in UTS #51),excluding Emoji_Component
     */
    FULLY_QUALIFIED,
    /**
     * a minimally-qualified emoji (see ED-18a in UTS #51)
     */
    MINIMALLY_QUALIFIED,
    /**
     * a unqualified emoji (See ED-19 in UTS #51)
     */
    UNQUALIFIED,
    /**
     * an Emoji_Component, excluding Regional_Indicators, ASCII, and non-Emoji.
     */
    COMPONENT
}
