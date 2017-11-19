package com.saantiaguilera.loquacious.observer

import android.app.Application
import android.content.Context
import android.content.Intent

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers

import java.util.ArrayList
import java.util.Locale

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class LocaleBroadcastReceiverTest {

    fun any() = Mockito.any(Locale::class.java) ?: Locale("en-US")

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "loquacious", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
    }

    @Test
    fun test_LocaleChanges_DispatchesToLoquacious() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hidrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })
        var loquacious = Mockito.spy(Loquacious.getInstance())
        ReflectionHelpers.setStaticField(Loquacious::class.java, "loquacious", loquacious)

        LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, Intent())

        Mockito.verify(loquacious).onLocaleChanged(any())
    }

    @Test
    fun test_LocaleChanges_SetsNewLocale() {
        Loquacious.initialize(RuntimeEnvironment.application, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hidrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {

                }.type)
            }
        })

        val context = Mockito.spy<Application>(RuntimeEnvironment.application)

        LocaleBroadcastReceiver().onReceive(context, Intent())

        // To avoid including power mock, this is my best change of verifying that we are trying to obtain
        // the current locale
        Mockito.verify<Context>(context).resources
    }

}
