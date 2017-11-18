package com.saantiaguilera.loquacious.persistence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.saantiaguilera.loquacious.model.Item;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.util.LocaleUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */
public class LoquaciousStore implements Store.Fetch, Store.Clear, Store.Commit {

    private static final String STORE_SHARED_PREFERENCES = LoquaciousStore.class.getName() + "_sharedPreferences";

    @NonNull
    private SharedPreferences sharedPreferences;
    @NonNull
    private Serializer serializer;

    public LoquaciousStore(@NonNull Context context, @NonNull Serializer serializer) {
        this.sharedPreferences = context.getSharedPreferences(STORE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        this.serializer = serializer;
    }

    @CheckResult
    private String formatKey(@NonNull String key) {
        Locale current = LocaleUtil.current();
        String language = current == null ? "nil" : current.getDisplayLanguage();
        return language + "_" + key;
    }

    @CheckResult
    private <Type> SharedPreferences.Editor put(@NonNull SharedPreferences.Editor editor, @NonNull Item<Type> item) {
        return editor.putString(formatKey(item.getKey()), serializer.serialize(item));
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public <Type> void put(@NonNull Item<Type> item) {
        put(sharedPreferences.edit(), item).apply();
    }

    @Override
    public <Type> void putAll(@NonNull List<Item<Type>> items) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Item<Type> item : items) {
            editor = put(editor, item);
        }
        editor.apply();
    }

    @Override
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    @Nullable
    @CheckResult
    @Override
    public <Type> Item<Type> fetch(@NonNull String name, @NonNull Class<Type> typeClass) {
        String value = sharedPreferences.getString(formatKey(name), null);

        if (value != null) {
            return serializer.hidrate(value, typeClass);
        }
        return null;
    }

}
