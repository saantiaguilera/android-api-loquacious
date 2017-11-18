package com.saantiaguilera.loquacious.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ibm.icu.text.PluralRules;

/**
 * Created by saguilera on 11/18/17.
 */

public enum Quantity {
    ZERO,
    ONE,
    TWO,
    FEW,
    MANY,
    OTHER;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    @Nullable
    public static Quantity from(@NonNull PluralRules rules, int quantity) {
        String plural = rules.select(quantity).toLowerCase();
        for (Quantity enumerable : values()) {
            if (enumerable.toString().contentEquals(plural)) {
                return enumerable;
            }
        }
        return null;
    }
}
