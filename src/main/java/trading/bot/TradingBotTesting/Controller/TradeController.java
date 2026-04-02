package trading.bot.TradingBotTesting.Controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final JdbcTemplate jdbcTemplate;

    public TradeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<Map<String, Object>> getTrades(
            @RequestParam(defaultValue = "LIVE") String mode
    ) {
        return jdbcTemplate.queryForList(
                "SELECT * FROM trade WHERE mode = ? ORDER BY timestamp DESC",
                mode
        );
    }

    @GetMapping("/live")
    public List<Map<String, Object>> getLiveTrades() {
        return jdbcTemplate.queryForList(
                "SELECT * FROM trade WHERE mode = 'LIVE' ORDER BY timestamp DESC"
        );
    }

    @GetMapping("/training")
    public List<Map<String, Object>> getTrainingTrades() {
        return jdbcTemplate.queryForList(
                "SELECT * FROM trade WHERE mode = 'TRAINING' ORDER BY timestamp DESC"
        );
    }
}