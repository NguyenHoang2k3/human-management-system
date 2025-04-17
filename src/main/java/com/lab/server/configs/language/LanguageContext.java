package com.lab.server.configs.language;

import java.util.Locale;

public class LanguageContext {
    private static final ThreadLocal<Locale> context = new ThreadLocal<>();

    public static void setLocale(Locale locale) {
        context.set(locale);
    }

    public static Locale getLocale() {
        return context.get() != null ? context.get() : Locale.ENGLISH;
    }

    public static void clear() {
        context.remove();
    }
}
