package com.saantiaguilera.locales.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.saantiaguilera.locales.util.LocaleUtil

import com.saantiaguilera.loquacious.Loquacious

/**
 * Created by saguilera on 11/18/17.
 */

class LocaleBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) =
            Loquacious.instance(LocaleUtil.setSystemLocale(context))

}
