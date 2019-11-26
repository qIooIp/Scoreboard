# Scoreboard
High performace 32 character scoreboard API implementation. Follow **Integration** and **Adapters** sections for easy implementation.


## Integration
```java
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
```

## Adapters
```java
public class ExampleScoreAdapter implements ScoreAdapter {

    @Override
    public String getTitle() {
        return ChatColor.GOLD + "Scoreboard API";
    }

    @Override
    public void updateLines(PlayerScoreboard scoreboard) {
        Player player = scoreboard.getPlayer();

        scoreboard.addLine(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------");

        scoreboard.addLine("&6Line 1");
        scoreboard.addLine("&6Line 2");
        scoreboard.addLine("&6Line 3");

        scoreboard.addLine("");

        scoreboard.addLine("&6&lYour name:");
        scoreboard.addLine("&7" + player.getName());

        scoreboard.addLine(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------");
    }
}
```
