package scoreboard.thread;


import scoreboard.PlayerScoreboard;
import scoreboard.ScoreboardManager;

public class ScoreboardThread extends Thread {

    private ScoreboardManager manager;

    private boolean running;
    private long interval;

    public ScoreboardThread(ScoreboardManager manager, long defaultInterval) {
        this.manager = manager;

        this.running = true;
        this.interval = defaultInterval;
    }

    public void setRunning(boolean value) {
        this.running = value;
    }

    public void setInterval(long value) {
        this.interval = value;
    }

    @Override
    public void run() {
        try {
            while(this.running) {
                this.manager.getScoreboards().forEach(PlayerScoreboard::update);

                Thread.sleep(this.interval);
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
