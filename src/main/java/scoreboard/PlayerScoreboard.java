package scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import scoreboard.adapter.ScoreAdapter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class PlayerScoreboard {

    private static final ScoreboardInput EMPTY_INPUT = new ScoreboardInput("$$$$$$", "$$$$$$");

    private final UUID playerUuid;

    private final Scoreboard scoreboard;
    private final Objective objective;

    private ScoreAdapter scoreAdapter;
    private Deque<ScoreboardInput> entries;

    private ScoreboardInput[] entryCache;
    private String[] teamNameCache;

    private int lastEntriesSize;
    private final AtomicBoolean update;

    PlayerScoreboard(Player player, ScoreAdapter scoreAdapter) {
        this.playerUuid = player.getUniqueId();

        this.scoreboard = player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()
        ? Bukkit.getScoreboardManager().getNewScoreboard() : player.getScoreboard();

        this.objective = this.getObjective();
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(scoreAdapter.getTitle());

        this.scoreAdapter = scoreAdapter;
        this.entries = new ArrayDeque<>();

        this.setupEntryCache();
        this.setupTeamNameCache();

        this.update = new AtomicBoolean(false);
        player.setScoreboard(this.scoreboard);
    }

    void unregister() {
        this.entries.clear();

        this.scoreboard.getObjectives().forEach(Objective::unregister);
        this.scoreboard.getTeams().forEach(Team::unregister);
        this.scoreboard.getEntries().forEach(this.scoreboard::resetScores);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerUuid);
    }

    private Objective getObjective() {
        Objective objective = this.scoreboard.getObjective("Scoreboard");
        return objective == null ? this.scoreboard.registerNewObjective("Scoreboard", "dummy") : objective;
    }

    private void setupEntryCache() {
        this.entryCache = new ScoreboardInput[15];
        IntStream.range(0, 15).forEach(i -> this.entryCache[i] = EMPTY_INPUT);
    }

    private void setupTeamNameCache() {
        this.teamNameCache = new String[15];
        IntStream.range(0, 15).forEach(i -> this.teamNameCache[i] = this.getTeamName(i));
    }

    public void update() {
        this.scoreAdapter.updateLines(this);
        if(!this.update.get() && this.lastEntriesSize == 0) return;

        int entriesSize = this.entries.size();

        for(int i = this.entries.size(); i > 0; i--) {
            ScoreboardInput input = this.entries.pollFirst();
            if(input == null) return;
            if(this.entryCache[i].equals(input)) continue;

            String teamName = this.teamNameCache[i-1];
            Team team = this.getTeam(teamName);

            if(!team.hasEntry(teamName)) team.addEntry(teamName);
            team.setPrefix(input.getPrefix());
            team.setSuffix(input.getSuffix());

            this.objective.getScore(teamName).setScore(i);
        }

        if(entriesSize < this.lastEntriesSize) {
            for(int i = entriesSize; i < this.lastEntriesSize; i++) {
                this.scoreboard.resetScores(this.teamNameCache[i]);
                this.entryCache[i] = EMPTY_INPUT;
            }
        }

        this.lastEntriesSize = entriesSize;
        this.update.set(false);
    }

    public void addLine(String line) {
        line = ChatColor.translateAlternateColorCodes('&', line);

        String prefix;
        String suffix;

        if(line.length() <= 16) {
            prefix = line;
            suffix = "";
        } else {
            int split = line.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;

            prefix = line.substring(0, split);
            suffix = ChatColor.getLastColors(prefix) + line.substring(split);
        }

        if(suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }

        this.entries.addLast(new ScoreboardInput(prefix, suffix));
        this.update.set(true);
    }

    private Team getTeam(String name) {
        synchronized(this.scoreboard) {
            Team team = this.scoreboard.getTeam(name);
            return team == null ? this.scoreboard.registerNewTeam(name) : team;
        }
    }

    private String getTeamName(int index) {
        return ChatColor.values()[index / 10].toString() + ChatColor
        .values()[index % 10].toString() + ChatColor.RESET.toString();
    }

    public ScoreAdapter getScoreAdapter() {
        return this.scoreAdapter;
    }

    public void setScoreAdapter(ScoreAdapter adapter) {
        this.scoreAdapter = adapter;
    }
}
