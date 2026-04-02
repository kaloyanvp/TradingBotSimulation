package trading.bot.TradingBotTesting.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trading.bot.TradingBotTesting.Services.AccountService;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Map<String, Double> getAccount() {
        return Map.of("balance", accountService.getBalance());
    }
}