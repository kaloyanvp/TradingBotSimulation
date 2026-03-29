package trading.bot.TradingBotTesting;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final JdbcTemplate jdbc;

    public AccountService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public double getBalance() {
        Double result = jdbc.queryForObject(
                "SELECT balance FROM account WHERE id = 1",
                Double.class
        );

        return result != null ? result : 0.0;
    }

    public void updateBalance(double newBalance) {
        jdbc.update(
                "UPDATE account SET balance = ? WHERE id = 1",
                newBalance
        );
    }

    public void addToBalance(double amount) {
        double current = getBalance();
        updateBalance(current + amount);
    }

    public void subtractFromBalance(double amount) {
        double current = getBalance();
        updateBalance(current - amount);
    }
}
