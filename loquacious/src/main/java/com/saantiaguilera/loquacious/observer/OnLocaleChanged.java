package com.saantiaguilera.loquacious.observer;

import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */
public interface OnLocaleChanged {
    void onLocaleChanged(@NonNull Locale locale);
}
