package scoreboard;

class ScoreboardInput {

    private String prefix;
    private String suffix;

    ScoreboardInput(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    String getPrefix() {
        return this.prefix;
    }

    String getSuffix() {
        return this.suffix;
    }

    boolean equals(ScoreboardInput other) {
        return this.prefix.length() == other.getPrefix().length() && this.prefix.equals(other.getPrefix())
            && this.suffix.length() == other.getSuffix().length() && this.suffix.equals(other.getSuffix());
    }
}
