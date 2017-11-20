package com.saantiaguilera.loquacious.persistence

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.CheckResult
import com.saantiaguilera.loquacious.model.Item

import com.saantiaguilera.loquacious.parse.GsonSerializer
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil
import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/18/17.
 */
class LoquaciousStore(context: Context) : Store.Fetch, Store.Clear, Store.Commit {

    private val sharedPreferences: SharedPreferences
    private val serializer: Serializer by lazy { GsonSerializer() }

    init {
        this.sharedPreferences = context.getSharedPreferences(STORE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    @CheckResult
    private fun formatKey(key: String): String = (LocaleUtil.current()?.displayLanguage ?: "nil") + "_" + key

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
    override fun <Type> fetch(name: String, klass: KClass<*>): Type? =
            sharedPreferences.getString(formatKey(name), "").let {
                if (it.isEmpty()) null else serializer.hydrate(it, klass)
            }

    companion object {
        private val STORE_SHARED_PREFERENCES = LoquaciousStore::class.java.name + "_sharedPreferences"
    }

}
