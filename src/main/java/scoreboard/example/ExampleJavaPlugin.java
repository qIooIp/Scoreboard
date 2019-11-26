package scoreboard.example;

import org.bukkit.plugin.java.JavaPlugin;
import scoreboard.ScoreboardManager;

public class ExampleJavaPlugin extends JavaPlugin {

    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        // Initialize scoreboard manager
        this.scoreboardManager = new ScoreboardManager(this, new ExampleScoreAdapter());

        // Set update interval in milliseconds (default is 1000)
        this.scoreboardManager.setUpdateInterval(2000L);
    }

    @Override
    public void onDisable() {
        // Clean up all scoreboards
        this.scoreboardManager.disable();
    }
}
