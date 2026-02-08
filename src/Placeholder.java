public final class Placeholder {
    private final String identifier;

    /* The Position of each variable parsed.
       Including '${' and '}'. */
    private final Position position;

    public Placeholder(String identifier, Position position) {
        this.identifier = identifier;
        this.position = position;
    }

    public boolean isValidPlaceholder() {
        return this.identifier != null && this.position != null && this.position.isThisValidPosition();
    }

    public String getIdentifier() {
        return identifier;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
