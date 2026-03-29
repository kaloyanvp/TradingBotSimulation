package trading.bot.TradingBotTesting;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BotScheduler {

    private final TradingBotService botService;
    private boolean running = false;

    public BotScheduler(TradingBotService botService) {
        this.botService = botService;
    }

    @Scheduled(fixedRate = 5000)
    public void run() {
        if (this.running) botService.tick();
    }

    // These must be public!
    public void start() {
        this.running = true;
        this.run();
    }
    public void stop() {
        this.running = false;
    }
}
