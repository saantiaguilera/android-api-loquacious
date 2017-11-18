package com.saantiaguilera.loquacious.model

import com.ibm.icu.text.PluralRules

/**
 * Created by saguilera on 11/18/17.
 */

enum class Quantity {
    ZERO,
    ONE,
    TWO,
    FEW,
    MANY,
    OTHER;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }

    companion object {

        fun from(rules: PluralRules, quantity: Int): Quantity? {
            val plural = rules.select(quantity.toDouble()).toLowerCase()
            for (enumerable in values()) {
                if (enumerable.toString().contentEquals(plural)) {
                    return enumerable
                }
            }
            return null
        }
    }
}
