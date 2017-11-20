package com.saantiaguilera.locales.observer

import android.app.Application
import android.content.Context
import android.content.Intent
import com.saantiaguilera.locales.util.LocaleUtil
import com.saantiaguilera.loquacious.Loquacious
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

    private fun any() = Mockito.any(Locale::class.java) ?: Locale.ENGLISH

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "initialized", false)
    }

    @Test
    fun test_LocaleChanges_DispatchesToLoquacious() {
        Loquacious.initialize(RuntimeEnvironment.application)
        val loquacious = Mockito.spy(Loquacious.instance)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", loquacious)

        LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, Intent())

        Mockito.verify(loquacious)(any())
    }

}
