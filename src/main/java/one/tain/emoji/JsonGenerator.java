package one.tain.emoji;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This app generate the emoji json from https://unicode.org/emoji/charts/full-emoji-list.html ;)
 * <p/>
 * Run with:
 * mvn exec:java -Dexec.mainClass="one.tain.emoji.JsonGenerator"
 */
public class JsonGenerator {
    private static final Logger LOGGER = Logger.getLogger(JsonGenerator.class.getName());
    private static final String ARGS_NAME_ONLINE_URL = "url";
    private static final String ARGS_NAME_SAVE_PATH = "save_url";
    private static final String ARGS_NAME_EMOJI_JSON_PATH = "emoji_path";
    private static final String ARGS_NAME_EMOJI_I18N_JSON_PATH = "emoji_i18n_path";
    private static final String STRING_SYMBOL_EQUAL = "=";
    private static final String EMOJI_REMOTE_ONLINE_URL = "https://beyond.tain.one/full-emoji-list.html";
    //    private static final String EMOJI_REMOTE_ONLINE_URL = "https://unicode.org/emoji/charts/full-emoji-list.html";
    private static Map<String, String> ARGS_MAP;

    public static void main(String[] args) throws IOException {
        ARGS_MAP = argsParser(args);
        Document root = getDocument();

        JSONArray emojis = new JSONArray();

        Map<String, JSONObject> emojiMap = getJsonMapFromEmojiJson(ARGS_MAP.get(ARGS_NAME_EMOJI_JSON_PATH));
        Map<String, String> emojiI18nMap = getI18nMapFromEmojiI18nJson(ARGS_MAP.get(ARGS_NAME_EMOJI_I18N_JSON_PATH));
        String aliasBigHead = null, aliasMediumHead = null;
        for (Element trTag : root.getElementsByTag("tr")) {
            Element bighead = trTag.select("th.bighead>a").first();
            if (!Objects.isNull(bighead)) {
                aliasBigHead = bighead.attr("name");
                continue;
            }
            Element mediumhead = trTag.select("th.mediumhead>a").first();
            if (!Objects.isNull(mediumhead)) {
                aliasMediumHead = mediumhead.attr("name");
                continue;
            }
            Elements tdTags = trTag.children();
            if (!tdTags.get(1).hasClass("code")) {
                continue;
            }
            String desc = Objects.requireNonNull(tdTags.last()).text().replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\p{Sc}\\s]", "");

            String emojiChar = tdTags.get(2).text();
            if (tdTags.get(1).text().endsWith("U+FE0F U+20E3")) {
                emojiChar = new StringBuilder(emojiChar).deleteCharAt(1).toString();
            }
            JSONObject emoji = new JSONObject();
            if (!emojiMap.containsKey(emojiChar)) {
                emoji.put("emojiChar", emojiChar);
                emoji.put("emoji", convertEmoji2Unicode(emojiChar));
                emoji.put("description", emojiI18nMap.getOrDefault(emojiChar, desc));
                emoji.put("aliases", desc.replace(" ", "_"));

                emoji.put("tags", Arrays.asList(aliasBigHead, aliasMediumHead));
            } else {
                emoji = emojiMap.get(emojiChar);
                emoji.put("description", emojiI18nMap.getOrDefault(emoji.getString("emojiChar"),
                        emoji.getString("description")));
                emoji.put("emoji", convertEmoji2Unicode(emoji.getString("emojiChar")));
            }
            emojis.put(emoji);
        }

        String emojiJson = emojis.toString(4).replace("/", "\\\\")
                .replace("\\^\\^u", "\\\\u");

        File emojiFile = new File(ARGS_MAP.getOrDefault(ARGS_NAME_SAVE_PATH, System.getProperty("java.io.tmpdir")
                + File.separator + "emoji.json"));
        LOGGER.info("save to: " + emojiFile.getAbsolutePath());
        Files.write(emojiFile.toPath(), Collections.singleton(emojiJson), StandardCharsets.UTF_8);
    }

    /**
     * convert emoji to unicode
     *
     * @param emoji emoji char
     * @return emoji's unicode
     */
    private static String convertEmoji2Unicode(String emoji) {
        char[] chars = emoji.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            builder.append("^^u");
            builder.append(Integer.toHexString(0x10000 | c).substring(1).toUpperCase());
        }
        return builder.toString();
    }

    /**
     * jsoup document builder
     *
     * @return access url and get body when args without `path` arg else read local file by `path`
     * @throws IOException io exception
     */
    private static Document getDocument() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ARGS_MAP.getOrDefault(ARGS_NAME_ONLINE_URL, EMOJI_REMOTE_ONLINE_URL))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            assert response.body() != null;
            return Jsoup.parse(response.body().string());
        }
    }

    /**
     * args parser
     *
     * @param args command args
     * @return args Map
     */
    private static Map<String, String> argsParser(String[] args) {
        if (Objects.isNull(args) || args.length == 0) {
            return Collections.emptyMap();
        }
        ARGS_MAP = new HashMap<>(args.length);
        int index;
        for (String arg : args) {
            index = arg.indexOf(STRING_SYMBOL_EQUAL);
            if (index <= 0) {
                continue;
            }
            ARGS_MAP.put(arg.substring(0, index), arg.substring(index + 1));
        }
        return ARGS_MAP;
    }

    private static Map<String, JSONObject> getJsonMapFromEmojiJson(String emojiPath) {
        if (Objects.isNull(emojiPath) || emojiPath.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, JSONObject> emojiMap;
        try {
            JSONArray emojiArray = new JSONArray(String.join("", Files.readAllLines(new File(emojiPath).toPath())));
            emojiMap = new HashMap<>(emojiArray.length());
            for (Object json : emojiArray) {
                JSONObject emoji = (JSONObject) json;
                emojiMap.put(emoji.getString("emojiChar"), emoji);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            emojiMap = Collections.emptyMap();
        }
        return emojiMap;
    }

    private static Map<String, String> getI18nMapFromEmojiI18nJson(String emojiI18nPath) {
        if (Objects.isNull(emojiI18nPath) || emojiI18nPath.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> emojiMap;
        try {
            JSONArray emojiArray = new JSONArray(String.join("", Files.readAllLines(new File(emojiI18nPath).toPath())));
            emojiMap = new HashMap<>(emojiArray.length());
            for (Object json : emojiArray) {
                JSONObject emoji = (JSONObject) json;
                emojiMap.put(emoji.getString("emojiChar"), emoji.getString("description"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            emojiMap = Collections.emptyMap();
        }
        return emojiMap;
    }
}
