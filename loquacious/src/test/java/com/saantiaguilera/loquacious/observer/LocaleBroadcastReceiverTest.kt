package com.saantiaguilera.loquacious.observer

import android.app.Application
import android.content.Context
import android.content.Intent
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.util.LocaleUtil
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers
import java.util.*

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class LocaleBroadcastReceiverTest {

    fun any() = Mockito.any(Locale::class.java) ?: Locale("en-US")

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
    }

    @Test
    fun test_LocaleChanges_DispatchesToLoquacious() {
        Loquacious.initialize(RuntimeEnvironment.application)
        val loquacious = Mockito.spy(Loquacious.instance)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", loquacious)

        LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, Intent())

        Mockito.verify(loquacious)(any())
    }

    @Test
    fun test_LocaleChanges_SetsNewLocale() {
        Loquacious.initialize(RuntimeEnvironment.application)

        val context = Mockito.spy<Application>(RuntimeEnvironment.application)

        LocaleBroadcastReceiver().onReceive(context, Intent())

        // To avoid including power mock, this is my best change of verifying that we are trying to obtain
        // the current locale
        Mockito.verify<Context>(context).resources
    }

}
