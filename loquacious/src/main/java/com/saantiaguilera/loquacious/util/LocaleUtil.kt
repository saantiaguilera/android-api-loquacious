package com.saantiaguilera.loquacious.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build

import java.util.Locale

/**
 * Created by saguilera on 11/18/17.
 */

class LocaleUtil @Throws(IllegalAccessException::class)
private constructor() {

    init {
        throw IllegalAccessException("Cant instantiate this")
    }

    companion object {

        @Volatile private var current: Locale? = null

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(context: Context): Locale {
            val locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.resources.configuration.locales.get(0)
                current = locale
                return locale
            }
            locale = context.resources.configuration.locale
            current = locale
            return locale
        }

        fun current(): Locale? {
            return current
        }
    }

}
