package trading.bot.TradingBotTesting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final BotScheduler scheduler;

    public BotController(BotScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/start")
    public void start() {
        System.out.println("Starting Bot");
        this.scheduler.start();
    }

    @PostMapping("/pause")
    public void pause() {
        System.out.println("Pausing Bot");
        this.scheduler.stop();
    }
}
