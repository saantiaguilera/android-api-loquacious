package com.saantiaguilera.loquacious;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.saantiaguilera.loquacious.observer.LocaleBroadcastReceiver;
import com.saantiaguilera.loquacious.observer.OnLocaleChanged;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.persistence.Store;
import com.saantiaguilera.loquacious.resource.Resources;

import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */

public final class Loquacious implements OnLocaleChanged {

    private static Loquacious instance;

    private Resources resources;

    private Loquacious(@NonNull Context context, @NonNull Serializer serializer) {
        context.registerReceiver(new LocaleBroadcastReceiver(),
                new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
        resources = new Resources(context, serializer);
    }

    public static void initialize(@NonNull Context context, @NonNull Serializer serializer) {
        instance = new Loquacious(context, serializer);
    }

    @NonNull
    public static Loquacious getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Loquacious is not initialized!");
        }
        return instance;
    }

    @NonNull
    public static Resources getResources() {
        return getInstance().resources;
    }

    @Override
    public void onLocaleChanged(@NonNull Locale locale) {

    }

}
