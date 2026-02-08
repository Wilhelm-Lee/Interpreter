public class Position {
    private int offset = -1;
    private int length = -1;

    public Position(int length) {
        new Position(0, length);
    }

    public Position(int offset, int length) {
        if (length < 0) {
            return;
        }

        this.offset = offset;
        this.length = length;
    }

    public boolean isThisValidPosition() {
        return offset >= 0 && length >= 0;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public int getEnd() {
        return offset + length;
    }

    @Override
    public String toString() {
        return "(%d:%d)".formatted(offset, length);
    }
}

