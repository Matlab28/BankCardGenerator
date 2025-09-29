package com.example.bankcardgenerator.util;

import java.math.BigInteger;

public final class IbanUtil {

    private IbanUtil(){}

    /**
     * Compute IBAN check digits for: countryCode + bban
     * This implementation follows the IBAN algorithm:
     *  - Move countryCode + "00" to end: bban + countryCode + "00"
     *  - Convert letters to numbers (A=10 ... Z=35)
     *  - Compute mod 97 and subtract from 98
     */
    public static String computeIban(String countryCode, String bban) {
        String rearranged = bban + countryCode + "00";
        StringBuilder numeric = new StringBuilder();
        for (char ch : rearranged.toCharArray()) {
            if (Character.isLetter(ch)) {
                int val = Character.toUpperCase(ch) - 'A' + 10;
                numeric.append(val);
            } else {
                numeric.append(ch);
            }
        }
        BigInteger bigInt = new BigInteger(numeric.toString());
        int mod = bigInt.mod(BigInteger.valueOf(97)).intValue();
        int checkDigits = 98 - mod;
        return String.format("%02d", checkDigits);
    }

    public static boolean validateIban(String iban) {
        String normalized = iban.replaceAll("\\s+", "");
        String country = normalized.substring(0, 2);
        String check = normalized.substring(2, 4);
        String bban = normalized.substring(4);
        String computedCheck = computeIban(country, bban);
        return check.equals(computedCheck);
    }
}
