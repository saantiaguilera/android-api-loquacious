package com.saantiaguilera.loquacious.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */

public final class LocaleUtil {

    private LocaleUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Cant instantiate this");
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getLocale(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        }
        return context.getResources().getConfiguration().locale;
    }

}
