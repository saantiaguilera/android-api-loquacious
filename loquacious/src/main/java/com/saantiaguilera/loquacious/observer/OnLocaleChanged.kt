package com.saantiaguilera.loquacious.observer

import java.util.Locale

/**
 * Created by saguilera on 11/18/17.
 */
interface OnLocaleChanged {
    fun onLocaleChanged(locale: Locale)
}
