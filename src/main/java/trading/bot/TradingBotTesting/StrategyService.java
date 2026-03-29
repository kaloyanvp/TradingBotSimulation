package trading.bot.TradingBotTesting;

import org.springframework.stereotype.Service;

@Service
public class StrategyService {

    public boolean shouldBuy(double price) {
        return price < 30000;
    }

    public boolean shouldSell(double price) {
        return price > 35000;
    }
}
