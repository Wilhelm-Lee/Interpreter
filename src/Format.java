import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{[a-zA-Z_]\\w*}");

    /* Original text. */
    private final String content;
    private final Placeholder[] placeholders;

    public Format(String content) {
        this.content = content;
        this.placeholders = ParsePlaceholders(content);
    }

    private static Placeholder[] ParsePlaceholders(String rawline) {
        if (rawline == null || rawline.isEmpty()) {
            return null;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(rawline);
        int offset = 0;
        ArrayList<Placeholder> placeholders = new ArrayList<>(0);
        while (matcher.find(offset)) {
            String substring = rawline.substring(matcher.start(), matcher.end());

            if (substring.isEmpty()) {
                continue;
            }

            /* Cropping edges. */
            /* ${identifier} -> identifier */
            substring = substring.substring(2, substring.length() - 1);  // error?

            placeholders.add(new Placeholder(substring, new Position(matcher.start(), matcher.end() - matcher.start())));

            offset = matcher.end();
        }

        return placeholders.toArray(new Placeholder[0]);
    }

    public String getContent() {
        return content;
    }

    public Placeholder[] getPlaceholders() {
        return placeholders;
    }

    @Override
    public String toString() {
        return this.content;
    }
}
