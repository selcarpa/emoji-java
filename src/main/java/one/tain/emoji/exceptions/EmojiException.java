package one.tain.emoji.exceptions;

public class EmojiException extends RuntimeException {
    public EmojiException() {
    }

    public EmojiException(String message) {
        super(message);
    }

    public EmojiException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmojiException(Throwable cause) {
        super(cause);
    }

    public EmojiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
