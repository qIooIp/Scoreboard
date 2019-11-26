package scoreboard.adapter;

import scoreboard.PlayerScoreboard;

public interface ScoreAdapter {

    String getTitle();

    void updateLines(PlayerScoreboard scoreboard);
}
