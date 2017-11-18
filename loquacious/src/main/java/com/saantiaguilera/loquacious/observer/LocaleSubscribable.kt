package com.saantiaguilera.loquacious.observer

/**
 * Created by saguilera on 11/18/17.
 */

interface LocaleSubscribable {
    fun subscribe(subscriptor: OnLocaleChanged): Boolean
    fun unsubscribe(subscriptor: OnLocaleChanged): Boolean
}
