package com.example.bankcardgenerator.service;

import com.example.bankcardgenerator.util.LuhnUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class CardGeneratorService {

    private final Random random = new SecureRandom();

    /**
     * Generate a card number using given BIN/prefix and total length (including check digit).
     * Example: prefix "4" (Visa), length 16 => will produce 16-digit Visa-like number.
     */
    public String generateCardNumber(String prefix, int totalLength) {
        if (prefix == null) prefix = "";
        if (totalLength <= prefix.length() + 1) {
            throw new IllegalArgumentException("totalLength must be at least prefix length + 1");
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

    public boolean validate(String cardNumber) {
        return LuhnUtil.validateLuhn(cardNumber);
    }
}
