package com.saantiaguilera.loquacious.observer

import java.util.*

/**
 * Created by saguilera on 11/18/17.
 */
interface LocaleSubscribable {
    fun subscribe(subscriptor: OnLocaleChanged): Boolean
    fun unsubscribe(subscriptor: OnLocaleChanged): Boolean
}

typealias OnLocaleChanged = (Locale) -> Unit