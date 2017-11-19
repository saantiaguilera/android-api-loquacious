package com.saantiaguilera.loquacious

import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.observer.LocaleBroadcastReceiver
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil
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
    }

    @Test
    fun test_CantUseWithoutInit() {
        try {
            Loquacious.instance
            Assert.fail()
        } catch (ignored: Exception) {
        }

        try {
            Loquacious.resources
            Assert.fail()
        } catch (ignored: Exception) {
        }

    }

    @Test
    fun test_Initialization_CreatesInstance() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {}.type)
            }
        })

        Assert.assertNotNull(Loquacious.instance)
    }

    @Test
    fun test_Resources_Exist_IfInitialized() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        Assert.assertNotNull(Loquacious.resources)
    }

    @Test
    fun test_Adding_Subscriptor() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        Assert.assertTrue(Loquacious.instance.subscribe({}))
    }

    @Test
    fun test_Adding_Subscriptor_WontHappenTwice() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        var onLocaleChanged = { _: Locale -> }
        onLocaleChanged = Mockito.spy(onLocaleChanged)

        Assert.assertTrue(Loquacious.instance.subscribe(onLocaleChanged))
        Assert.assertFalse(Loquacious.instance.subscribe(onLocaleChanged))
    }

    @Test
    fun test_Removing_Subscriptor() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        var onLocaleChanged = { _: Locale -> }
        onLocaleChanged = Mockito.spy(onLocaleChanged)

        Assert.assertTrue(Loquacious.instance.subscribe(onLocaleChanged))
        Assert.assertTrue(Loquacious.instance.unsubscribe(onLocaleChanged))
    }

    @Test
    fun test_Removing_NonExistentSubscriptor_IsTrue() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        var onLocaleChanged = { _: Locale -> }
        onLocaleChanged = Mockito.spy(onLocaleChanged)

        Assert.assertTrue(Loquacious.instance.unsubscribe(onLocaleChanged))
    }

    @Test
    fun test_LocaleChanged_NotifiesSubscriptors() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        var onLocaleChanged = { _: Locale -> }
        onLocaleChanged = Mockito.spy(onLocaleChanged)

        Assert.assertTrue(Loquacious.instance.subscribe(onLocaleChanged))

        LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, Intent())

        Mockito.verify(onLocaleChanged).invoke(any())
    }

}
