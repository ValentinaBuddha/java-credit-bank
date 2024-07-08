package ru.neoflex.statement.util;

public class StringPatterns {

    public static final String LATIN_ALPHABET = "[a-zA-Z]{2,30}";

    public static final String EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static final String PASSPORT_SERIES = "[\\d]{4}";

    public static final String PASSPORT_NUMBER = "[\\d]{6}";

    private StringPatterns() {
    }
}
