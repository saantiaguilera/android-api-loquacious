package com.saantiaguilera.loquacious

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.saantiaguilera.loquacious.observer.LocaleBroadcastReceiver
import com.saantiaguilera.loquacious.observer.LocaleSubscribable
import com.saantiaguilera.loquacious.observer.OnLocaleChanged
import com.saantiaguilera.loquacious.resource.Resources
import com.saantiaguilera.loquacious.util.LocaleUtil
import java.util.*

/**
 * Entry point for the Loquacious library.
 */
class Loquacious private constructor(context: Context) : OnLocaleChanged,
        LocaleSubscribable {

    private val resources: Resources
    private val onLocaleChangedList by lazy { ArrayList<(Locale) -> Unit>() }

    init {
        context.registerReceiver(LocaleBroadcastReceiver(),
                IntentFilter(Intent.ACTION_LOCALE_CHANGED))
        resources = Resources(context)
    }

    /**
     * Invokes a locale change in loquacious library
     */
    override operator fun invoke(locale: Locale) {
        onLocaleChangedList.forEach { it(locale) }
    }

    /**
     * Subscribe to locale changes
     * Careful the listener is kept as a strong reference!
     */
    override fun subscribe(subscriptor: (Locale) -> Unit): Boolean =
            !onLocaleChangedList.contains(subscriptor) && onLocaleChangedList.add(subscriptor)

    /**
     * Unsubscribe to locale changes
     */
    override fun unsubscribe(subscriptor: (Locale) -> Unit): Boolean =
            !onLocaleChangedList.contains(subscriptor) || onLocaleChangedList.add(subscriptor)

    companion object {

        /**
         * Instance getter
         */
        lateinit var instance: Loquacious
            private set

        /**
         * Get the resources associated with loquacious.
         * If you have wrapped the context and attached it into the base,
         * then this resources are the same your application/activity/etc uses
         */
        val resources
            get() = Loquacious.instance.resources

        /**
         * Initialize the library
         */
        fun initialize(context: Context) {
            LocaleUtil.setSystemLocale(context)
            instance = Loquacious(context)
        }

        /**
         * Wraps the context in a new one. Useful if you want to use the remote resources
         * in XMLs or natively as activity.resources instead of Loquacious.resources
         *
         * This method will initialize loquacious
         */
        fun wrap(context: Context): Context {
            initialize(context)
            return ContextWrapper.wrap(context)
        }

    }

}