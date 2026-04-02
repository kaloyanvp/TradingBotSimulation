package trading.bot.TradingBotTesting.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trading.bot.TradingBotTesting.Services.PortfolioService;

import java.util.Map;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public Map<String, Double> getPortfolio() {
        return Map.of("quantity", portfolioService.getQuantity("BTC"));
    }
}