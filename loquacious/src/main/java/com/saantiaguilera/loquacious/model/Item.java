package com.saantiaguilera.loquacious.model;

import android.support.annotation.NonNull;

import com.saantiaguilera.loquacious.util.Mangler;

/**
 * Created by saguilera on 11/18/17.
 */
public class Item<StoreElement> {

    @NonNull
    private final String key;

    @NonNull
    private final StoreElement value;

    public Item(@NonNull String key, @NonNull StoreElement value) {
        this.key = Mangler.mangle(key);
        this.value = value;
    }

    public Item(@NonNull String key, Quantity quantity, @NonNull StoreElement value) {
        this.key = Mangler.mangle(key, quantity);
        this.value = value;
    }

    @NonNull
    public final String getKey() {
        return key;
    }

    @NonNull
    public final StoreElement getValue() {
        return value;
    }

}
