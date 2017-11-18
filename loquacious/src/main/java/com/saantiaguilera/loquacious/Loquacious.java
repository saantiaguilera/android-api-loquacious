package com.saantiaguilera.loquacious;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.saantiaguilera.loquacious.observer.LocaleBroadcastReceiver;
import com.saantiaguilera.loquacious.observer.LocaleSubscribable;
import com.saantiaguilera.loquacious.observer.OnLocaleChanged;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.persistence.Store;
import com.saantiaguilera.loquacious.resource.Resources;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */

public final class Loquacious implements OnLocaleChanged, LocaleSubscribable {

    private static Loquacious instance;

    @NonNull
    private Resources resources;
    @NonNull
    private List<OnLocaleChanged> onLocaleChangedList;

    private Loquacious(@NonNull Context context, @NonNull Serializer serializer) {
        onLocaleChangedList = new ArrayList<>();
        context.registerReceiver(new LocaleBroadcastReceiver(),
                new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
        resources = new Resources(context, serializer);;
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
        for (OnLocaleChanged subscriptor : onLocaleChangedList) {
            subscriptor.onLocaleChanged(locale);
        }
    }

    @Override
    public boolean subscribe(@NonNull OnLocaleChanged subscriptor) {
        return !onLocaleChangedList.contains(subscriptor) && onLocaleChangedList.add(subscriptor);
    }

    @Override
    public boolean unsubscribe(@NonNull OnLocaleChanged subscriptor) {
        return !onLocaleChangedList.contains(subscriptor) || onLocaleChangedList.add(subscriptor);
    }

}
