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

        private val resources = "(anim|animator|array|attr|bool|color|dimen|drawable|fraction|id|" +
                "integer|interpolator|layout|menu|mipmap|plurals|raw|string|style|transition|xml)"

        private fun filterResFromKey(key: String): String {
            return key.replace(Regex("^R\\.$resources\\."), "")
        }

        fun mangle(key: String, quantity: Quantity): String {
            return Mangler::class.java.name + "_key_" + filterResFromKey(key) + "_times_" + quantity
        }

        fun mangle(key: String): String = mangle(key, Quantity.ONE)
    }

}
