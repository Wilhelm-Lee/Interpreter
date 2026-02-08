public class MacroPattern {
    private Format target = null;
    private Format replacement = null;

    public MacroPattern(String raw) {
        if (raw == null || raw.isEmpty()) {
            return;
        }

        raw = raw.trim();

        /* #pattern target $= replacement */
        /* [#] [pattern] [target] [$=] [replacement] */
        if (raw.charAt(0) != '#') {
            return;
        }

        raw = raw.substring(1);

        raw = raw.replaceFirst("pattern", "")
                 .trim();

        /* target $= replacement */
        /* [target] [$=] [replacement] */
        final int separatorIndex = raw.indexOf("$=");
        if (separatorIndex < 0) {
            return;
        }

        this.target = new Format(raw.substring(0, separatorIndex - 1));
        this.replacement = new Format(raw.substring(separatorIndex + "$=".length() + 1));
    }

    public boolean isThisValidMacroPattern() {
        return (target != null && replacement != null);
    }

    public Format getTarget() {
        return target;
    }

    public Format getReplacement() {
        return replacement;
    }

    public String generateTargetPositionIndicators() {
        return target.generatePositionIndicators();
    }

    public String generateReplacementPositionIndicators() {
        return replacement.generatePositionIndicators();
    }

    @Override
    public String toString() {
        return "#pattern " + this.target + " $= " + this.replacement;
    }
}
