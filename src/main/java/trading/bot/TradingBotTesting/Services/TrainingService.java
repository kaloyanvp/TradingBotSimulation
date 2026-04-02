package trading.bot.TradingBotTesting.Services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import trading.bot.TradingBotTesting.Candle;

import java.util.List;

@Service
public class TrainingService {

    private final TradingBotService botService;
    private final MarketService marketService;
    private final AccountService accountService;
    private final PortfolioService portfolioService;
    private final JdbcTemplate jdbcTemplate;

    public TrainingService(TradingBotService botService,
                           MarketService marketService,
                           AccountService accountService,
                           PortfolioService portfolioService,
                           JdbcTemplate jdbcTemplate) {
        this.botService = botService;
        this.marketService = marketService;
        this.accountService = accountService;
        this.portfolioService = portfolioService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void runBacktest() {
        jdbcTemplate.update("DELETE FROM trade WHERE mode = 'TRAINING'");
        //System.out.println("Starting backtest...");

        // Reset account for clean simulation
        accountService.updateBalance(10000);
        portfolioService.updateQuantity("BTC", 0);

        List<Candle> candles = marketService.getHistoricalData("BTCUSDT", "1h", 500);

        for (int i = 0; i < candles.size(); i++) {
            double price = candles.get(i).getClosePrice();

            botService.tickWithPrice(price);
        }

        // Final evaluation
        double finalBalance = accountService.getBalance();
        double btc = portfolioService.getQuantity("BTC");
        double lastPrice = candles.get(candles.size() - 1).getClosePrice();

        double totalValue = finalBalance + (btc * lastPrice);

        double initial = 10000;
        double profit = totalValue - initial;
        double percent = (profit / initial) * 100;

        System.out.println("Final Value: " + totalValue);
        System.out.println("Return: " + percent + "%");

        System.out.println("Backtest complete!");
    }
}