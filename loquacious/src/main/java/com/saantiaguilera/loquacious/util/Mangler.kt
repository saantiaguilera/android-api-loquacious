package com.saantiaguilera.loquacious.util

import com.saantiaguilera.loquacious.model.Quantity

/**
 * Created by saguilera on 11/18/17.
 */
class Mangler @Throws(IllegalAccessException::class) private constructor() {

    init {
        throw IllegalAccessException("This class cant be instantiated")
    }

    companion object {

        private fun clean(string: String) = string.replace(Regex("[^a-zA-Z0-9]"), "_")

        fun mangle(key: String, quantity: Quantity): String = "key_" + clean(key) + "_times_" + quantity

        fun mangle(key: String): String = mangle(key, Quantity.ONE)

    }

}
