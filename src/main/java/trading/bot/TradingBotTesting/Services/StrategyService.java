package trading.bot.TradingBotTesting.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StrategyService {

    private final MarketService marketService;
    private List<Double> priceHistory = new ArrayList<>();

    public StrategyService(MarketService marketService) {

        this.marketService = marketService;
    }

    public boolean shouldBuy(String symbol) {
        List<Double> prices = marketService.getRecentPrices(symbol, 20);

        double shortMA = calculateMA(prices, 5);
        double longMA = calculateMA(prices, 20);

        return shortMA > longMA;
    }

    public boolean shouldSell(String symbol) {
        List<Double> prices = marketService.getRecentPrices(symbol, 20);

        double shortMA = calculateMA(prices, 5);
        double longMA = calculateMA(prices, 20);

        return shortMA < longMA;
    }

    public double getTrendStrength(String symbol) {
        List<Double> prices = marketService.getRecentPrices(symbol, 20);

        double shortMA = calculateMA(prices, 5);
        double longMA = calculateMA(prices, 20);

        if (longMA == 0)
            return 0;

        return Math.abs(shortMA - longMA) / longMA;
    }

    public double getTrendStrengthFromHistory() {
        if (priceHistory.size() < 20) return 0;

        double shortMA = calculateMA(priceHistory, 5);
        double longMA = calculateMA(priceHistory, 20);

        return Math.abs(shortMA - longMA) / longMA;
    }

    private double calculateMA(List<Double> prices, int period) {
        if (prices.size() < period) return 0;

        double sum = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            sum += prices.get(i);
        }

        return sum / period;
    }

    public void addPrice(double price) {
        priceHistory.add(price);
    }

    public boolean shouldBuyFromHistory() {
        if (priceHistory.size() < 20) return false;

        double shortMA = calculateMA(priceHistory, 5);
        double longMA = calculateMA(priceHistory, 20);

        double threshold = 0.0005;

        return (shortMA - longMA) / longMA > threshold;
    }

    public boolean shouldSellFromHistory() {
        if (priceHistory.size() < 20) return false;

        double shortMA = calculateMA(priceHistory, 5);
        double longMA = calculateMA(priceHistory, 20);

        double threshold = 0.0005;

        return (longMA - shortMA) / longMA > threshold;
    }

}