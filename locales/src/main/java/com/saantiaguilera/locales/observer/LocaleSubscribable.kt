package com.saantiaguilera.locales.observer

import com.saantiaguilera.loquacious.Loquacious
import java.util.*

/**
 * Created by saguilera on 11/18/17.
 */
typealias OnLocaleChanged = (Locale) -> Unit

/**
 * Define loquacious methods for subscribing and registering locales
 */
private val Loquacious.onLocaleChangedList by lazy { ArrayList<OnLocaleChanged>() }

/**
 * Invokes a locale change in loquacious library
 */
internal operator fun Loquacious.invoke(locale: Locale) {
    onLocaleChangedList.forEach { it(locale) }
}

/**
 * Subscribe to locale changes
 * Careful the listener is kept as a strong reference!
 */
fun Loquacious.subscribe(subscriptor: OnLocaleChanged): Boolean =
        !onLocaleChangedList.contains(subscriptor) && onLocaleChangedList.add(subscriptor)

/**
 * Unsubscribe to locale changes
 */
fun Loquacious.unsubscribe(subscriptor: OnLocaleChanged): Boolean =
        !onLocaleChangedList.contains(subscriptor) || onLocaleChangedList.remove(subscriptor)
