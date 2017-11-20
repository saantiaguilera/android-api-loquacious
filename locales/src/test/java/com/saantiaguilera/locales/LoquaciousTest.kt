package com.saantiaguilera.locales

import android.content.Intent
import com.saantiaguilera.locales.observer.LocaleBroadcastReceiver
import com.saantiaguilera.locales.observer.subscribe
import com.saantiaguilera.locales.observer.unsubscribe
import com.saantiaguilera.locales.util.LocaleUtil
import com.saantiaguilera.loquacious.Loquacious
import org.junit.After
import org.junit.Assert
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
class LoquaciousTest {

    fun any() = Mockito.any(Locale::class.java) ?: Locale("en-US")

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "initialized", false)
    }

    @Test
    fun test_Adding_Subscriptor() {
        Loquacious.initialize(RuntimeEnvironment.application)

        Assert.assertTrue(Loquacious.instance.subscribe({}))
    }

    @Test
    fun test_Adding_Subscriptor_WontHappenTwice() {
        Loquacious.initialize(RuntimeEnvironment.application)

        val onLocaleChanged = { _: Locale -> }

        Assert.assertTrue(Loquacious.instance.subscribe(onLocaleChanged))
        Assert.assertFalse(Loquacious.instance.subscribe(onLocaleChanged))
    }

    @Test
    fun test_Removing_Subscriptor() {
        Loquacious.initialize(RuntimeEnvironment.application)

        val onLocaleChanged = { _: Locale -> }

        Assert.assertTrue(Loquacious.instance.subscribe(onLocaleChanged))
        Assert.assertTrue(Loquacious.instance.unsubscribe(onLocaleChanged))
    }

    @Test
    fun test_Removing_NonExistentSubscriptor_IsTrue() {
        Loquacious.initialize(RuntimeEnvironment.application)

        val onLocaleChanged = { _: Locale -> }

        Assert.assertTrue(Loquacious.instance.unsubscribe(onLocaleChanged))
    }

    @Test
    fun test_LocaleChanged_NotifiesSubscriptors() {
        Loquacious.initialize(RuntimeEnvironment.application)

        val onLocaleChanged = { _: Locale ->
            throw Exception()
        }

        Assert.assertTrue(Loquacious.instance.subscribe(onLocaleChanged))

        try {
            LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, Intent())
            Assert.fail()
        } catch (ignored: Exception) {
        } finally {
            Loquacious.instance.unsubscribe(onLocaleChanged)
        }
    }

}