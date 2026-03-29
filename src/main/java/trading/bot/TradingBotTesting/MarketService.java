package trading.bot.TradingBotTesting;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MarketService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double getCurrentPrice(String symbol) {
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;

        Map<String, String> response = restTemplate.getForObject(url, Map.class);

        return Double.parseDouble(response.get("price"));
    }
}
