package one.tain.emoji;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import one.tain.emoji.exceptions.EmojiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class EmojiProvider {
    protected static final Map<String, Emoji> EMOJIS_BY_ALIAS = new HashMap<>();
    protected static final Map<String, List<Emoji>> EMOJIS_BY_TAG = new HashMap<>();
    protected static final List<Emoji> ALL_EMOJIS = new ArrayList<>();

    public static EmojiProvider DEFAULT() {
        return new EmbeddedProvider();
    }

    public List<Emoji> getForTag(String tag) {
        return EMOJIS_BY_TAG.get(tag);
    }

    public Emoji getForAlias(String alias) {
        return EMOJIS_BY_ALIAS.get(alias);
    }

    public Emoji getForUnicode(String unicode) {
        for (Emoji emoji : ALL_EMOJIS) {
            if (emoji.getUnicode().equals(unicode)) {
                return emoji;
            }
        }
        return null;
    }


    /**
     * Returns all the {@link Emoji}s loaded.
     *
     * @return
     */
    public abstract List<Emoji> getAllEmojis();

    public static class EmbeddedProvider extends EmojiProvider {
        public EmbeddedProvider() {

            ObjectMapper objectMapper = new ObjectMapper();
            ArrayType emojiArrayType = objectMapper.getTypeFactory().constructArrayType(EmojiDto.class);
            try {
                List<Emoji> emojis = objectMapper.readValue(EmbeddedProvider.class.getResource("/emojis.json"), emojiArrayType);
                ALL_EMOJIS.addAll(emojis);
            } catch (IOException e) {
                throw new EmojiException(e);
            }
        }

        @Override
        public List<Emoji> getAllEmojis() {
            return ALL_EMOJIS;
        }
    }

}
