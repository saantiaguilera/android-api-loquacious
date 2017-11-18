package com.saantiaguilera.loquacious.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.util.LocaleUtil

/**
 * Created by saguilera on 11/18/17.
 */

class LocaleBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Loquacious.getInstance().onLocaleChanged(LocaleUtil.setSystemLocale(context))
    }

}
