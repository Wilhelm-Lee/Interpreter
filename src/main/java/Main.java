import java.io.*;
import java.util.ArrayList;

public class Main {

    private static int getFirstNonWhitespaceIndexForSubstringing(String str) {
        /* Handle null or empty input safely. */
        if (str == null || str.isEmpty()) {
            return 0;
        }

        for (int i = 0; i < str.length(); i++) {
            /* Check if the character is NOT a whitespace. */
            if (!Character.isWhitespace(str.charAt(i))) {
                return i;
            }
        }

        /* Return 0 if no non-whitespace character is found. */
        return 0;
    }

    private static String[] Compact(String[] lines) {
        if (lines == null || lines.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean isPreviousLineNeedsMultiLineReading = false;
        boolean isCurrentLineNeedsMultiLineReading = false;
        ArrayList<String> compactedLines = new ArrayList<>(0);
        boolean isMacroContent = false;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] == null || lines[i].isEmpty() || lines[i].length() <= 1) {
                continue;
            }

            String line = lines[i];
            if (i > 0) {
                isPreviousLineNeedsMultiLineReading = isCurrentLineNeedsMultiLineReading;
            }
            isCurrentLineNeedsMultiLineReading = (line.charAt(line.length() - 1) == '\\');

            /* Remove trailing '\\'. */
            if (isCurrentLineNeedsMultiLineReading) {
                line = line.substring(0, line.length() - 1);
                line = line.concat(" ");  // Append a space for readability.
            }

            if (isMacroContent) {
                /* Remove leading indentations. */
                line = line.substring(getFirstNonWhitespaceIndexForSubstringing(line));
            }

            /* Entering. */
            if (!isPreviousLineNeedsMultiLineReading && isCurrentLineNeedsMultiLineReading) {
                isMacroContent = true;
                sb.append(line);
            }

            /* Inside. */
            if (isPreviousLineNeedsMultiLineReading && isCurrentLineNeedsMultiLineReading) {
                sb.append(line);
            }

            /* Exiting. */
            if (isPreviousLineNeedsMultiLineReading && !isCurrentLineNeedsMultiLineReading) {
                isMacroContent = false;
                sb.append(line);
                compactedLines.add(sb.toString());
                sb = new StringBuilder();
            }

            /* Outside. */
        }

        return compactedLines.toArray(new String[0]);
    }

    private static String[] ReadLines(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        ArrayList<String> readlines = new ArrayList<>(0);
        try (FileInputStream stream = new FileInputStream(filename);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                readlines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File \"" + filename + "\" is not found.");
            return null;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }

        return readlines.toArray(new String[0]);
    }

    private static MacroPattern[] ParseMacroPatterns(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        String[] lines = ReadLines(filename);
        if (lines == null) {
            return null;
        }

        String[] compacted = Compact(lines);
        if (compacted == null) {
            return null;
        }

        ArrayList<MacroPattern> patterns = new ArrayList<>(0);
        for (String s : compacted) {
            final MacroPattern current = new MacroPattern(s);
            if (!current.isThisValidMacroPattern()) {
                continue;
            }

            patterns.add(current);
        }

        return patterns.toArray(new MacroPattern[0]);
    }

    private static void PrintPositionsOfPlaceholdersOfMacroPatterns(final MacroPattern[] patterns) {
        if (patterns == null || patterns.length == 0) {
            return;
        }

        for (MacroPattern pattern : patterns) {
            if (!pattern.isThisValidMacroPattern()) {
                continue;
            }

            Placeholder[] placeholders = pattern.getTarget().getPlaceholders();
            if (placeholders == null) {
                continue;
            }

            for (Placeholder holder : placeholders) {
                if (!holder.isValidPlaceholder()) {
                    continue;
                }

                System.out.printf("%s: \"%s\"%s", holder.getPosition(),
                        pattern.getTarget().getContent().substring(holder.getPosition().getOffset(),
                                holder.getPosition().getOffset() + holder.getPosition().getLength()),
                        System.lineSeparator());
            }
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            System.err.println("USAGE: <macro_definintion_file> <macro_application_file>");
            return;
        }

        MacroPattern[] patterns = ParseMacroPatterns(args[0]);

        for (MacroPattern pattern : patterns) {
            System.out.println(pattern.getTarget());
            System.out.println(pattern.generateTargetPositionIndicators());

            System.out.println(pattern.getReplacement());
            System.out.println(pattern.generateReplacementPositionIndicators());
        }

        PrintPositionsOfPlaceholdersOfMacroPatterns(patterns);
    }
}


































