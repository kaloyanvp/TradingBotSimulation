package trading.bot.TradingBotTesting.Controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/overview")
public class OverviewController {

    private final JdbcTemplate jdbcTemplate;

    public OverviewController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/trades")
    public List<Map<String, Object>> getTradesForOverview(
            @RequestParam(defaultValue = "LIVE") String mode
    ) {
        return jdbcTemplate.queryForList("""
            SELECT type, quantity, price, timestamp, mode
            FROM trade
            WHERE mode = ?
            ORDER BY timestamp ASC
        """, mode);
    }
}