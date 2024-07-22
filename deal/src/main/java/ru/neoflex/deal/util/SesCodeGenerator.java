package ru.neoflex.deal.util;

import java.util.Random;

public class SesCodeGenerator {

    private static final Random RANDOM = new Random();

    public static int generateSesCode() {
        return 100000 + RANDOM.nextInt(900000);
    }

    private SesCodeGenerator() {
    }
}
