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
}
