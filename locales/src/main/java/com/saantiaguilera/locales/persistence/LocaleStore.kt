package com.saantiaguilera.locales.persistence

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.support.annotation.CheckResult
import com.saantiaguilera.locales.observer.LocaleBroadcastReceiver

import com.saantiaguilera.locales.parse.GsonSerializer
import com.saantiaguilera.locales.parse.Serializer
import com.saantiaguilera.locales.util.LocaleUtil
import com.saantiaguilera.loquacious.persistence.Store
import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/18/17.
 */
class LocaleStore(context: Context) : Store {

    private val sharedPreferences: SharedPreferences
    private val serializer: Serializer by lazy { GsonSerializer() }

    init {
        this.sharedPreferences = context.getSharedPreferences(STORE_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        LocaleUtil.setSystemLocale(context)
        context.registerReceiver(LocaleBroadcastReceiver(),
                IntentFilter(Intent.ACTION_LOCALE_CHANGED))
    }

    @CheckResult
    private fun formatKey(key: String): String = (LocaleUtil.current()?.toString() ?: "nil") + "_" + key

    @SuppressLint("CommitPrefEdits")
    override fun <Type> put(key: String, item: Type, klass: KClass<*>) = with(sharedPreferences.edit()) {
        putString(formatKey(key), serializer.serialize(item, klass))
        apply()
    }

    override fun <Type> putAll(kv: List<Pair<String, Type>>, klass: KClass<*>) = with(sharedPreferences.edit()) {
        kv.forEach { (key,item) -> putString(formatKey(key), serializer.serialize(item, klass)) }
        apply()
    }

    override fun clear() = sharedPreferences.edit().clear().apply()

    @CheckResult
    override fun <Type> fetch(key: String, klass: KClass<*>): Type? =
            sharedPreferences.getString(formatKey(key), "").let {
                if (it.isEmpty()) null else serializer.hydrate(it, klass)
            }

    companion object {
        private val STORE_SHARED_PREFERENCES = LocaleStore::class.java.name + "_sharedPreferences"
    }

}
