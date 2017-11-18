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
    private fun formatKey(key: String): String {
        val current = LocaleUtil.current()
        val language = if (current == null) "nil" else current.displayLanguage
        return language + "_" + key
    }

    @CheckResult
    private fun <Type> put(editor: SharedPreferences.Editor, item: Item<Type>): SharedPreferences.Editor {
        return editor.putString(formatKey(item.key), serializer.serialize(item))
    }

    @SuppressLint("CommitPrefEdits")
    override fun <Type> put(item: Item<Type>) {
        put(sharedPreferences.edit(), item).apply()
    }

    override fun <Type> putAll(items: List<Item<Type>>) {
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        for (item in items) {
            editor = put(editor, item)
        }
        editor.apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    @CheckResult
    override fun <Type> fetch(name: String, typeClass: Class<Type>): Item<Type>? {
        val value = sharedPreferences.getString(formatKey(name), null)

        return if (value != null) {
            serializer.hidrate(value, typeClass)
        } else null
    }

    companion object {

        private val STORE_SHARED_PREFERENCES = LoquaciousStore::class.java.name + "_sharedPreferences"
    }

}
