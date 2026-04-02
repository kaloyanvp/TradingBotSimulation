package trading.bot.TradingBotTesting;

import org.springframework.stereotype.Component;
import trading.bot.TradingBotTesting.Services.TradingBotService;

@Component
public class BotScheduler {

    private final TradingBotService botService;
    private Thread botThread;
    private volatile boolean running = false;

    public BotScheduler(TradingBotService botService) {
        this.botService = botService;
    }

    public void start() {
        if (running) return;
        running = true;
        System.out.println("Starting Bot");

        botThread = new Thread(() -> {
            while (running) {
                try {
                    botService.tick(); // run a tick

                    // Wait 5 seconds between ticks
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        botThread.start();

    }

    public void stop() {
        running = false;
        if (botThread != null) botThread.interrupt();
        System.out.println("Bot stopped");
    }

    public boolean isRunning() {
        return running;
    }
}