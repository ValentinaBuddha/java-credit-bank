package ru.neoflex.deal.util;

import java.security.SecureRandom;

public class SesCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static int generateSesCode() {
        return 100000 + RANDOM.nextInt(900000);
    }

    private SesCodeGenerator() {
    }
}
