package com.saantiaguilera.loquacious.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.saantiaguilera.loquacious.Loquacious;
import com.saantiaguilera.loquacious.util.LocaleUtil;

/**
 * Created by saguilera on 11/18/17.
 */

public class LocaleBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Loquacious.getInstance().onLocaleChanged(LocaleUtil.getLocale(context));
    }

}
