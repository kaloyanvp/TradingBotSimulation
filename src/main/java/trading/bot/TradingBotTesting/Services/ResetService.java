package trading.bot.TradingBotTesting.Services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResetService {

    private final JdbcTemplate jdbcTemplate;
    private final AccountService accountService;

    public ResetService(JdbcTemplate jdbcTemplate, AccountService accountService) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountService = accountService;
    }

    public void resetAll() {
        jdbcTemplate.update("DELETE FROM trade");
        accountService.updateBalance(10000);

        jdbcTemplate.update("""
            UPDATE portfolio
            SET quantity = 0
            WHERE asset_id = (SELECT id FROM asset WHERE symbol = 'BTC')
        """);
    }
}