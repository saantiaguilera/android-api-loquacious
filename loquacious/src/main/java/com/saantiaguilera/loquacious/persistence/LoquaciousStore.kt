package com.saantiaguilera.loquacious.persistence

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.CheckResult

import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil

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
    override fun <Type> put(key: String, item: Item<Type>) = with(sharedPreferences.edit()) {
        putString(formatKey(key), serializer.serialize(item))
        apply()
    }

    override fun <Type> putAll(key: List<String>, items: List<Item<Type>>) = with(sharedPreferences.edit()) {
        key.zip(items).forEach { (key,item) -> putString(formatKey(key), serializer.serialize(item)) }
        apply()
    }

    override fun clear() = sharedPreferences.edit().clear().apply()

    @CheckResult
    override fun <Type> fetch(name: String, typeClass: Class<Type>): Item<Type>? =
            sharedPreferences.getString(formatKey(name), "").let {
                if (it.isEmpty()) null else serializer.hydrate(it, typeClass)
            }

    companion object {
        private val STORE_SHARED_PREFERENCES = LoquaciousStore::class.java.name + "_sharedPreferences"
    }

}
