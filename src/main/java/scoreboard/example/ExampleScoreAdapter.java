package scoreboard.example;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import scoreboard.PlayerScoreboard;
import scoreboard.adapter.ScoreAdapter;

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
