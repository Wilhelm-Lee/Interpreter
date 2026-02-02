import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

    private static ArrayList<MacroPattern> macroPatternRegistries;

    private static String ReadFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        return ReadFile(new File(filename));
    }

    private static String ReadFile(File file) {
        if (file == null) {
            return null;
        }

        byte[] content;
        try (FileInputStream stream = new FileInputStream(file)) {
            content = stream.readAllBytes();
        } catch (FileNotFoundException e) {
            System.err.println("File \"" + file.getAbsolutePath() + "\" is not found.");
            return null;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }

        return new String(content);
    }

    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            System.err.println("USAGE: <macro_definintion_file> <macro_application_file>");
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
            System.err.println("File \"" + args[0] + "\" is not found.");
            return;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return;
        }

        final String finalContent = Replace(ReadFile(args[1]));

        System.out.println(Replace(finalContent));
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

    /* Apply @macroPatternRegistries in @content recursively. */
    private static String ReplaceSingular(String content, MacroPattern pattern) {
        if (content == null || content.isEmpty() || pattern == null) {
            return null;
        }

        return content.replaceAll(pattern.getTarget(), pattern.getReplacement());
    }

    private static String Replace(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        for (MacroPattern pattern : macroPatternRegistries) {
            if (!content.contains(pattern.getTarget())) {
                continue;
            }

            content = ReplaceSingular(content, pattern);
        }

        return content;
    }
}
