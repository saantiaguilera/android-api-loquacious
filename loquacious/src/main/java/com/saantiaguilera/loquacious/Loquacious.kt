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

import java.util.ArrayList
import java.util.Locale

/**
 * Created by saguilera on 11/18/17.
 */

class Loquacious private constructor(context: Context, serializer: Serializer) : OnLocaleChanged, LocaleSubscribable {

    private val resources: Resources
    private val onLocaleChangedList: MutableList<OnLocaleChanged>

    init {
        onLocaleChangedList = ArrayList()
        context.registerReceiver(LocaleBroadcastReceiver(),
                IntentFilter(Intent.ACTION_LOCALE_CHANGED))
        resources = Resources(context, serializer)
    }

    override fun onLocaleChanged(locale: Locale) {
        for (subscriptor in onLocaleChangedList) {
            subscriptor.onLocaleChanged(locale)
        }
    }

    override fun subscribe(subscriptor: OnLocaleChanged): Boolean {
        return !onLocaleChangedList.contains(subscriptor) && onLocaleChangedList.add(subscriptor)
    }

    override fun unsubscribe(subscriptor: OnLocaleChanged): Boolean {
        return !onLocaleChangedList.contains(subscriptor) || onLocaleChangedList.add(subscriptor)
    }

    companion object {

        private var loquacious: Loquacious? = null

        fun initialize(context: Context, serializer: Serializer) {
            LocaleUtil.setSystemLocale(context)
            loquacious = Loquacious(context, serializer)
        }

        fun getInstance(): Loquacious {
            if (loquacious == null) {
                throw IllegalStateException("Loquacious is not initialized!")
            }
            return loquacious as Loquacious
        }

        fun getResources(): Resources {
            return getInstance().resources
        }
    }

}
