package com.example.bankcardgenerator.controller;

import com.example.bankcardgenerator.service.BankAccountGeneratorService;
import com.example.bankcardgenerator.service.CardGeneratorService;
import com.example.bankcardgenerator.service.UserAccountGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate")
public class GeneratorController {

    private final CardGeneratorService cardService;
    private final BankAccountGeneratorService bankService;
    private final UserAccountGeneratorService userService;

    public GeneratorController(CardGeneratorService cardService,
                               BankAccountGeneratorService bankService,
                               UserAccountGeneratorService userService) {
        this.cardService = cardService;
        this.bankService = bankService;
        this.userService = userService;
    }

    @GetMapping("/card")
    public ResponseEntity<?> generateCard(
            @RequestParam(defaultValue = "4") String prefix,
            @RequestParam(defaultValue = "16") int length) {
        String card = cardService.generateCardNumber(prefix, length);
        return ResponseEntity.ok(card);
    }

    @GetMapping("/iban")
    public ResponseEntity<?> generateIban(
            @RequestParam(defaultValue = "AZ") String country,
            @RequestParam(defaultValue = "20") int bbanLength) {
        String iban = bankService.generateIban(country, bbanLength);
        return ResponseEntity.ok(iban);
    }

    @GetMapping("/account")
    public ResponseEntity<?> generateBankAccount(
            @RequestParam(defaultValue = "16") int digits) {
        String acc = bankService.generateNumericAccountWithMod97(digits);
        return ResponseEntity.ok(acc);
    }

    @GetMapping("/user")
    public ResponseEntity<?> generateUser(
            @RequestParam(required = false) String first,
            @RequestParam(required = false) String last,
            @RequestParam(defaultValue = "12") int passLen) {

        String username = userService.generateUsername(first, last);
        String password = userService.generatePassword(passLen, true, true);
        String hashed = userService.hashPassword(password);

        return ResponseEntity.ok(
                new UserResponse(username, password, hashed)
        );
    }

    public static class UserResponse {
        public String username;
        public String plainPassword;
        public String passwordHash;

        public UserResponse(String username, String plainPassword, String passwordHash) {
            this.username = username;
            this.plainPassword = plainPassword;
            this.passwordHash = passwordHash;
        }
    }
}
