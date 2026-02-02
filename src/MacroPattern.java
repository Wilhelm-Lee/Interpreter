/* #pattern target $= replacement */
public class MacroPattern {
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
        return ("'%s' $= '%s'").formatted(target, replacement);
    }
}
