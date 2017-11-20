package com.saantiaguilera.locales.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.annotation.RestrictTo
import com.saantiaguilera.locales.util.LocaleUtil

import com.saantiaguilera.loquacious.Loquacious

/**
 * Created by saguilera on 11/18/17.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class LocaleBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) =
            Loquacious.instance(LocaleUtil.setSystemLocale(context))

}
