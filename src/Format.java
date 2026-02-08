import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    public Position[] getPositions() {
        Position[] positions = new Position[this.placeholders.length];
        for (int i = 0, placeholdersLength = placeholders.length; i < placeholdersLength; i++) {
            if (!placeholders[i].isValidPlaceholder()) {
                continue;
            }

            positions[i] = placeholders[i].getPosition();
        }

        return positions;
    }

    public String generatePositionIndicators() {
        final Position[] positions = getPositions();

        final int total = content.length();

        char[] buffer = new char[total];
        Arrays.fill(buffer, ' ');

        for (Position position : positions) {
            buffer[position.getOffset()] = '^';
            for (int i = position.getOffset() + 1; i < position.getEnd(); i++) {
                buffer[i] = '~';
            }
        }

        return new String(buffer);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
