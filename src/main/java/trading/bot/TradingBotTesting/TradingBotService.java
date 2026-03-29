package trading.bot.TradingBotTesting;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradingBotService {

    private final AccountService accountService;
    private final PortfolioService portfolioService;
    private final MarketService marketService;
    private final StrategyService strategyService;
    private final JdbcTemplate jdbcTemplate;

    public TradingBotService(AccountService accountService,
                             PortfolioService portfolioService,
                             MarketService marketService,
                             StrategyService strategyService,
                             JdbcTemplate jdbc) {
        this.accountService = accountService;
        this.portfolioService = portfolioService;
        this.marketService = marketService;
        this.strategyService = strategyService;
        this.jdbcTemplate = jdbc;
    }


    public void run(String... args) throws Exception {
        System.out.println("===== SIMULATING 5 BOT TICKS =====");

        for (int i = 1; i <= 5; i++) {
            System.out.println("\n--- Tick " + i + " ---");

            // Fetch current BTC price
            double price = marketService.getCurrentPrice("BTCUSDT");
            System.out.println("Current BTC Price: " + price);

            // Run one tick
//            tradingBotService.tick();

            // Print account balance and BTC holdings
            double balance = accountService.getBalance();
            double btcQty = portfolioService.getQuantity("BTC");
            System.out.println("Balance: " + balance);
            System.out.println("BTC Quantity: " + btcQty);
        }

        System.out.println("\n===== SIMULATION COMPLETE =====");
    }


    public void tick() {
        double price = marketService.getCurrentPrice("BTCUSDT");

        if (strategyService.shouldBuy(price)) {
            buy(price);
            System.out.println("Buy BTC Price: " + price);
        } else if (strategyService.shouldSell(price)) {
            System.out.println("Sell BTC Price: " + price);
            sell(price);
        }
    }

    private void buy(double price) {
        double balance = accountService.getBalance();
        System.out.println(accountService.getBalance());
        if (balance < 10) return;

        double amountToSpend = balance * 0.1;
        double quantity = amountToSpend / price;

        accountService.subtractFromBalance(amountToSpend);
        portfolioService.updateQuantity("BTC", quantity);

        saveTrade("BUY", quantity, price, 0);
        System.out.println("Successfully bought BTC at Price: " + price);
    }

    private void sell(double price) {
        double quantityOwned = portfolioService.getQuantity("BTC");

        if (quantityOwned <= 0) return;

        double quantityToSell = quantityOwned * 0.1;
        double value = quantityToSell * price;

        portfolioService.updateQuantity("BTC", -quantityToSell);
        accountService.addToBalance(value);

        saveTrade("SELL", quantityToSell, price, 0);
    }

    private void saveTrade(String type, double qty, double price, double profit) {

        System.out.println("Saving " + type + " " + qty + " " + price + " " + profit);
        jdbcTemplate.update(
                """
                INSERT INTO trade (asset_id, type, quantity, price, timestamp, profit_loss)
                VALUES ((SELECT id FROM asset WHERE symbol = 'BTC'), ?, ?, ?, NOW(), ?)
                """,
                type, qty, price, profit
        );
    }


}

