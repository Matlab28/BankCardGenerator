package com.example.bankcardgenerator.util;

public final class LuhnUtil {

    private LuhnUtil() {}

    public static int calculateCheckDigit(String numberWithoutCheckDigit) {
        int sum = 0;
        boolean doubleDigit = true; // start from rightmost (we will process reversed)
        for (int i = numberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int d = Character.digit(numberWithoutCheckDigit.charAt(i), 10);
            if (doubleDigit) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleDigit = !doubleDigit;
        }
        int mod = sum % 10;
        return (mod == 0) ? 0 : (10 - mod);
    }

    public static boolean validateLuhn(String fullNumber) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = fullNumber.length() - 1; i >= 0; i--) {
            int d = Character.digit(fullNumber.charAt(i), 10);
            if (doubleDigit) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }
}
