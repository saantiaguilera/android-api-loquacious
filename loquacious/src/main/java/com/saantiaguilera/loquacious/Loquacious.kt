package com.saantiaguilera.loquacious

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.saantiaguilera.loquacious.observer.LocaleBroadcastReceiver
import com.saantiaguilera.loquacious.observer.LocaleSubscribable
import com.saantiaguilera.loquacious.observer.OnLocaleChanged
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.resource.Resources
import com.saantiaguilera.loquacious.util.LocaleUtil
import java.util.*

/**
 * Created by saguilera on 11/18/17.
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

    override fun invoke(locale: Locale) {
        onLocaleChangedList.forEach { it(locale) }
    }

    override fun subscribe(subscriptor: (Locale) -> Unit): Boolean =
            !onLocaleChangedList.contains(subscriptor) && onLocaleChangedList.add(subscriptor)

    override fun unsubscribe(subscriptor: (Locale) -> Unit): Boolean =
            !onLocaleChangedList.contains(subscriptor) || onLocaleChangedList.add(subscriptor)

    companion object {

        lateinit var instance: Loquacious
            private set

        val resources
            get() = Loquacious.instance.resources

        fun initialize(context: Context) {
            LocaleUtil.setSystemLocale(context)
            instance = Loquacious(context)
        }

        /**
         * Wraps the context in a new one. Useful if you want to use the remote resources
         * in XMLs or natively as activity.resources instead of Loquacious.resources
         */
        fun wrap(context: Context) = ContextWrapper.wrap(context)

    }

}