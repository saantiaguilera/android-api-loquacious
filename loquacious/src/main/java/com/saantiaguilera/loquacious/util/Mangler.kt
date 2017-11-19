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

        fun mangle(key: String, quantity: Quantity?): String {
            if (quantity == null) {
                return mangle(key)
            }
            return Mangler::class.java.name + "_key_" + key + "_times_" + quantity
        }

        fun mangle(key: String): String = Mangler::class.java.name + "_key_" + key
    }

}
