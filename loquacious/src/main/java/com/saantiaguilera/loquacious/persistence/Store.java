package com.saantiaguilera.loquacious.persistence;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.saantiaguilera.loquacious.model.Item;

import java.util.List;

/**
 * Created by saguilera on 11/18/17.
 */
public interface Store {

    interface Fetch {
        @CheckResult
        @Nullable
        <Type> Item<Type> fetch(@NonNull String name, @NonNull Class<Type> typeClass);
    }

    interface Commit {
        <Type> void put(@NonNull Item<Type> item);

        <Type> void putAll(@NonNull List<Item<Type>> items);
    }

    interface Clear {
        void clear();
    }

}
