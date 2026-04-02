package trading.bot.TradingBotTesting.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import trading.bot.TradingBotTesting.Candle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MarketService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double getCurrentPrice(String symbol) {
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;

        Map<String, String> response = restTemplate.getForObject(url, Map.class);
        
        return Double.parseDouble(response.get("price"));
    }
    
    public List<Double> getRecentPrices(String symbol, int limit) {
        String url = "https://api.binance.com/api/v3/klines?symbol="
                + symbol + "&interval=1m&limit=" + limit;

        List<List<Object>> response = restTemplate.getForObject(url, List.class);

        List<Double> prices = new ArrayList<>();

        for (List<Object> candle : response) {
            double closePrice = Double.parseDouble((String) candle.get(4)); // close price
            prices.add(closePrice);
        }

        return prices;
    }

    public List<Candle> getHistoricalData(String symbol, String interval, int limit) {
        String url = "https://api.binance.com/api/v3/klines?symbol="
                + symbol + "&interval=" + interval + "&limit=" + limit;

        List<List<Object>> response = restTemplate.getForObject(url, List.class);

        List<Candle> candles = new ArrayList<>();

        for (List<Object> c : response) {
            candles.add(new Candle(
                    ((Number) c.get(0)).longValue(),
                    Double.parseDouble((String) c.get(4))
            ));
        }

        return candles;
    }
}


