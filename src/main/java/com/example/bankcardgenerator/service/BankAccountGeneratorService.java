package com.example.bankcardgenerator.service;

import com.example.bankcardgenerator.util.IbanUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class BankAccountGeneratorService {

    private final SecureRandom random = new SecureRandom();

    /**
     * Generate a numeric account number with specified length and append a simple MOD97 checksum (2 digits).
     * Result example: 1234567890123456-45  (you can format differently)
     */
    public String generateNumericAccountWithMod97(int lengthWithoutChecksum) {
        if (lengthWithoutChecksum < 4) throw new IllegalArgumentException("length too small");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengthWithoutChecksum; i++) {
            sb.append(random.nextInt(10));
        }

        String digits = sb.toString();
        java.math.BigInteger bi = new java.math.BigInteger(digits);
        int mod = bi.mod(java.math.BigInteger.valueOf(97)).intValue();
        int checksum = (97 - mod) % 97;
        return digits + String.format("%02d", checksum);
    }

    /**
     * Generate a generic IBAN given a country code and desired BBAN length.
     * NOTE: proper IBAN BBAN structure is country-specific. This method generates a random numeric BBAN of given length.
     * Example: generateIban("AZ", 20) => returns something like "AZ98 1234..."
     */
    public String generateIban(String countryCode, int bbanLength) {
        if (countryCode == null || countryCode.length() != 2) {
            throw new IllegalArgumentException("countryCode must be 2 letters");
        }
        StringBuilder bban = new StringBuilder();
        for (int i = 0; i < bbanLength; i++) bban.append(random.nextInt(10));
        String check = IbanUtil.computeIban(countryCode.toUpperCase(), bban.toString());
        return countryCode.toUpperCase() + check + bban.toString();
    }
}
