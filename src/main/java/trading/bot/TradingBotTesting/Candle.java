package trading.bot.TradingBotTesting;

public class Candle {

    private long timestamp;
    private double closePrice;

    public Candle(long timestamp, double closePrice) {
        this.timestamp = timestamp;
        this.closePrice = closePrice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getClosePrice() {
        return closePrice;
    }
}
