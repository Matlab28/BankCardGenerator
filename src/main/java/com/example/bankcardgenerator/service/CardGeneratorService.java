package com.example.bankcardgenerator.service;

import com.example.bankcardgenerator.constant.CardType;
import com.example.bankcardgenerator.util.LuhnUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class CardGeneratorService {

    private final Random random = new SecureRandom();

    /**
     * Generate a card number based on CardType and total length (including check digit).
     * VISA -> starts with '4'
     * MASTER_CARD -> starts with '2' or '5' (randomly chosen)
     *
     * If you want specific BIN/prefixes (like 2221-2720 or 51-55) we can extend to accept
     * more precise prefix ranges.
     */
    public String generateCardNumber(CardType cardType, int totalLength) {
        if (totalLength < 12) {
            throw new IllegalArgumentException("totalLength must be at least 12 (practical minimum).");
        }

        String prefix = choosePrefixFor(cardType);
        if (totalLength <= prefix.length() + 1) {
            throw new IllegalArgumentException("totalLength must be greater than prefix length + 1");
        }

        StringBuilder sb = new StringBuilder(prefix);
        int numRandomDigits = totalLength - prefix.length() - 1; // minus check digit
        for (int i = 0; i < numRandomDigits; i++) {
            sb.append(random.nextInt(10));
        }

        String withoutCheck = sb.toString();
        int check = LuhnUtil.calculateCheckDigit(withoutCheck);
        sb.append(check);
        return sb.toString();
    }

    private String choosePrefixFor(CardType cardType) {
        if (cardType == null) {
            // fallback - choose Visa by default
            return "4";
        }
        switch (cardType) {
            case VISA:
                // Visa numbers start with 4
                return "4";
            case MASTER_CARD:
                // Mastercard can start with 2 (part of 2221–2720 family) or 5 (51–55 family).
                // For simplicity choose '2' or '5' as the first digit; you can also provide more specific BINs.
                return random.nextBoolean() ? "2" : "5";
            default:
                return "4";
        }
    }

    public boolean validate(String cardNumber) {
        return LuhnUtil.validateLuhn(cardNumber);
    }
}
