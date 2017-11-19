package com.saantiaguilera.loquacious.persistence

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.CheckResult

import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil
import java.util.Locale

/**
 * Created by saguilera on 11/18/17.
 */
class LoquaciousStore(context: Context, private val serializer: Serializer) : Store.Fetch, Store.Clear, Store.Commit {

    private val sharedPreferences: SharedPreferences

    init {
        this.sharedPreferences = context.getSharedPreferences(STORE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    @CheckResult
    private fun formatKey(key: String): String = (LocaleUtil.current()?.displayLanguage ?: "nil") + "_" + key

    @SuppressLint("CommitPrefEdits")
    override fun <Type> put(item: Item<Type>) = with(sharedPreferences.edit()) {
        putString(formatKey(item.key), serializer.serialize(item))
        apply()
    }

    override fun <Type> putAll(items: List<Item<Type>>) = with(sharedPreferences.edit()) {
        items.forEach { item -> putString(formatKey(item.key), serializer.serialize(item)) }
        apply()
    }

    override fun clear() = sharedPreferences.edit().clear().apply()

    @CheckResult
    override fun <Type> fetch(name: String, typeClass: Class<Type>): Item<Type>? =
            sharedPreferences.getString(formatKey(name), "").let {
                if (it.isEmpty()) null else serializer.hidrate(it, typeClass)
            }

    companion object {
        private val STORE_SHARED_PREFERENCES = LoquaciousStore::class.java.name + "_sharedPreferences"
    }

}
