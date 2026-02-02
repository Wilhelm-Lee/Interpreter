import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    private static ArrayList<MacroPattern> macroPatternRegistries;

    /* #pattern target $= replacement */
    private static class MacroPattern {
        String target;
        String replacement;

        public MacroPattern(String target) {
            this.target = target;
            this.replacement = "";
        }

        public MacroPattern(String target, String replacement) {
            this.target = target;
            this.replacement = replacement;
        }

        public String getTarget() {
            return target;
        }

        public String getReplacement() {
            return replacement;
        }

        @Override
        public String toString() {
            return (
                "MacroPattern {" + System.lineSeparator() +
                "  .target='%s'," + System.lineSeparator() +
                "  .replacement='%s'" + System.lineSeparator() +
                "}" + System.lineSeparator()
            ).formatted(target, replacement);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide file to parse.");
            return;
        }

        macroPatternRegistries = new ArrayList<>(0);

        try (FileInputStream stream = new FileInputStream(args[0]);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final MacroPattern current = Parse(line);
                if (current == null) {
                    continue;
                }

                macroPatternRegistries.add(current);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File \"" + args[1] + "\" is not found.");
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    /* [#] [pattern] <target> [$=] <replacement> */
    private static MacroPattern Parse(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        content = content.trim();

        /* Not a preprocessor line. */
        if (content.length() <= 1 || content.charAt(0) != '#') {
            return null;
        }

        content = content.substring(1);

        StringTokenizer tokenizer = new StringTokenizer(content, " ");

        /* Not a pattern preprocessor. */
        if (tokenizer.nextToken().compareTo("pattern") != 0) {
            return null;
        }

        content = content.replaceFirst("pattern", "");

        /* <target> [$=] <replacement> */
        final int separatorIndex = content.indexOf("$=");

        /* No separator found -- Illegal pattern macro. */
        if (separatorIndex == -1) {
            return null;
        }

        final String target = content.substring(0, separatorIndex).trim();
        final String replacement = content.substring(separatorIndex + "$=".length()).trim();

        /* No available target parsed -- Illegal pattern macro. */
        if (target.isEmpty()) {
            return null;
        }

        return new MacroPattern(target, replacement);
    }
}




































