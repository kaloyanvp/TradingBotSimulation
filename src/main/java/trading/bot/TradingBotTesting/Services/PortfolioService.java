package trading.bot.TradingBotTesting.Services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private final JdbcTemplate jdbc;

    public PortfolioService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public double getQuantity(String symbol) {
        return jdbc.query(
                """
                SELECT quantity FROM portfolio p
                JOIN asset a ON p.asset_id = a.id
                WHERE a.symbol = ?
                """,
                rs -> rs.next() ? rs.getDouble("quantity") : 0.0,
                symbol
        );
    }

    public void updateQuantity(String symbol, double quantityChange) {
        jdbc.update(
                """
                UPDATE portfolio
                SET quantity = quantity + ?
                WHERE asset_id = (SELECT id FROM asset WHERE symbol = ?)
                """,
                quantityChange, symbol
        );
    }
}
