package trading.bot.TradingBotTesting.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trading.bot.TradingBotTesting.BotScheduler;
import trading.bot.TradingBotTesting.Services.ResetService;
import trading.bot.TradingBotTesting.Services.TrainingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final BotScheduler scheduler;
    private final TrainingService trainingService;
    private final ResetService resetService;

    public BotController(BotScheduler scheduler,TrainingService trainingService,ResetService resetService) {
        this.scheduler = scheduler;
        this.trainingService = trainingService;
        this.resetService = resetService;
    }

    @PostMapping("/start")
    public void start() {

        this.scheduler.start();
    }

    @PostMapping("/pause")
    public void pause() {
        System.out.println("Pausing Bot");
        this.scheduler.stop();
    }

    @GetMapping("/train")
    public String train() {
        trainingService.runBacktest();
        return "Training (backtest) complete";
    }
}
