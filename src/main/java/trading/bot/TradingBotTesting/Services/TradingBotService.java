package trading.bot.TradingBotTesting.Services;

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
                             JdbcTemplate jdbcTemplate) {
        this.accountService = accountService;
        this.portfolioService = portfolioService;
        this.marketService = marketService;
        this.strategyService = strategyService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void tick() {
        double price = marketService.getCurrentPrice("BTCUSDT");
        double balance = accountService.getBalance();
        double btcQty = portfolioService.getQuantity("BTC");

        if (balance <= 0 && btcQty <= 0) {
            System.out.println("Bot is broke! No funds to trade.");
            return;
        }

        double strength = strategyService.getTrendStrength("BTCUSDT");

        if (strength < 0.0004) {
            System.out.println("Trade signal too weak, waiting for next tick...");
            System.out.println(strategyService.getTrendStrength("BTCUSDT"));
            return;
        }

        if (strategyService.shouldBuy("BTCUSDT")) {
            buy(price, strength, "LIVE");
        } else if (strategyService.shouldSell("BTCUSDT")) {
            sell(price, strength, "LIVE");
        } else {
            System.out.println("No trade signal, waiting for next tick...");
        }
    }

    public void tickWithPrice(double price) {
        strategyService.addPrice(price);

        double balance = accountService.getBalance();
        double btcQty = portfolioService.getQuantity("BTC");

        if (balance <= 0 && btcQty <= 0) {
            System.out.println("Bot is broke!");
            return;
        }

        double strength = strategyService.getTrendStrengthFromHistory();

        if (strength < 0.001) return;

        if (strategyService.shouldBuyFromHistory()) {
            buy(price, strength, "TRAINING");
        } else if (strategyService.shouldSellFromHistory()) {
            sell(price, strength, "TRAINING");
        }
    }

    private void buy(double price, double strength, String mode) {
        double balance = accountService.getBalance();
        if (balance < 10) return;

        double amountToSpend = Math.max(balance * Math.min(strength, 0.3), 10);
        if (amountToSpend > balance) amountToSpend = balance;

        double quantity = amountToSpend / price;

        accountService.subtractFromBalance(amountToSpend);
        portfolioService.updateQuantity("BTC", quantity);

        saveTrade("BUY", quantity, price, 0, mode);

        System.out.printf("BUY | Mode: %s | Price: %.2f | Amount spent: %.2f | Qty: %.8f%n",
                mode, price, amountToSpend, quantity);
    }

    private void sell(double price, double strength, String mode) {
        double quantityOwned = portfolioService.getQuantity("BTC");
        if (quantityOwned <= 0) return;

        double quantityToSell = Math.max(quantityOwned * Math.min(strength, 0.3), 0.00001);
        if (quantityToSell > quantityOwned) quantityToSell = quantityOwned;

        double value = quantityToSell * price;

        portfolioService.updateQuantity("BTC", -quantityToSell);
        accountService.addToBalance(value);

        saveTrade("SELL", quantityToSell, price, 0, mode);

        System.out.printf("SELL | Mode: %s | Price: %.2f | Value gained: %.2f | Qty: %.8f%n",
                mode, price, value, quantityToSell);
    }

    private void saveTrade(String type, double qty, double price, double profit, String mode) {
        jdbcTemplate.update(
                """
                INSERT INTO trade (asset_id, type, quantity, price, timestamp, profit_loss, mode)
                VALUES ((SELECT id FROM asset WHERE symbol = 'BTC'), ?, ?, ?, NOW(), ?, ?)
                """,
                type, qty, price, profit, mode
        );
    }

}