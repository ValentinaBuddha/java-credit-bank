package ru.neoflex.gateway.util;

public class StringPatterns {

    public static final String ACCOUNT_NUMBER = "[\\d]{20}";

    public static final String INN = "^([\\d]{10}|[\\d]{12})$";

    private StringPatterns() {
    }
}
