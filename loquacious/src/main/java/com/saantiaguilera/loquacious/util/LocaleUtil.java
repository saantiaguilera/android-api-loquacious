package com.saantiaguilera.loquacious.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */

public final class LocaleUtil {

    @Nullable
    private static volatile Locale current;

    private LocaleUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Cant instantiate this");
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale setSystemLocale(@NonNull Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
            current = locale;
            return locale;
        }
        locale = context.getResources().getConfiguration().locale;
        current = locale;
        return locale;
    }

    @Nullable
    public static Locale current() {
        return current;
    }

}
