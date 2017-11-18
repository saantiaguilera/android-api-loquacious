package com.saantiaguilera.loquacious.parse;

import android.support.annotation.NonNull;

import com.saantiaguilera.loquacious.model.Item;

/**
 * Created by saguilera on 11/18/17.
 */
public interface Serializer {
    @NonNull
    <Type> String serialize(@NonNull Item<Type> item);

    @NonNull
    <Type> Item<Type> hidrate(@NonNull String string, Class<Type> classType);
}
