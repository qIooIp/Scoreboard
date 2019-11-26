package scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import scoreboard.adapter.ScoreAdapter;
import scoreboard.thread.ScoreboardThread;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager implements Listener {

    private Map<UUID, PlayerScoreboard> scoreboards;

    private ScoreAdapter scoreAdapter;
    private ScoreboardThread scoreboardThread;

    public ScoreboardManager(JavaPlugin plugin, ScoreAdapter scoreAdapter) {
        this.scoreboards = new HashMap<>();
        this.scoreAdapter = scoreAdapter;

        this.scoreboardThread = new ScoreboardThread(this, 1000L);
        this.scoreboardThread.start();

        Bukkit.getOnlinePlayers().forEach(this::loadScoreboard);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void disable() {
        Bukkit.getOnlinePlayers().forEach(this::unregisterScoreboard);

        this.scoreboards.clear();
        this.scoreboardThread.setRunning(false);
    }

    public PlayerScoreboard getScoreboard(Player player) {
        return this.scoreboards.get(player.getUniqueId());
    }

    public Collection<PlayerScoreboard> getScoreboards() {
        return this.scoreboards.values();
    }

    public void setUpdateInterval(long value) {
        this.scoreboardThread.setInterval(value);
    }

    public void setScoreAdapter(ScoreAdapter newAdapter) {
        this.scoreboards.values().forEach(scoreboard -> {
            if(scoreboard.getScoreAdapter() != this.scoreAdapter) return;

            scoreboard.setScoreAdapter(newAdapter);
        });

        this.scoreAdapter = newAdapter;
    }

    private void loadScoreboard(Player player) {
        PlayerScoreboard scoreboard = new PlayerScoreboard(player, this.scoreAdapter);
        this.scoreboards.put(player.getUniqueId(), scoreboard);
    }

    private void unregisterScoreboard(Player player) {
        PlayerScoreboard scoreboard = this.scoreboards.remove(player.getUniqueId());
        if(scoreboard == null) return;

        scoreboard.unregister();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.loadScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.unregisterScoreboard(event.getPlayer());
    }
}
