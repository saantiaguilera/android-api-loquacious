package com.saantiaguilera.loquacious.resource

import android.content.Context
import android.support.annotation.CheckResult

import com.ibm.icu.text.PluralRules
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.persistence.LoquaciousStore
import com.saantiaguilera.loquacious.persistence.Store
import com.saantiaguilera.loquacious.util.LocaleUtil
import com.saantiaguilera.loquacious.util.Mangler

/**
 * Created by saguilera on 11/18/17.
 */
class Resources(context: Context, serializer: Serializer) :
        android.content.res.Resources(
                context.assets,
                context.resources.displayMetrics,
                context.resources.configuration
        ), Store.Commit, Store.Clear {

    private val store: LoquaciousStore = LoquaciousStore(context, serializer)

    @CheckResult
    private fun <ReturnType> fetch(entryName: String, returnTypeClass: Class<ReturnType>): ReturnType? =
            store.fetch(entryName, returnTypeClass)?.value

    @CheckResult
    fun <ReturnType> get(id: Int, returnTypeClass: Class<ReturnType>): ReturnType? =
            fetch(Mangler.mangle(getResourceEntryName(id)), returnTypeClass)

    @CheckResult
    fun <ReturnType> get(id: Int, quantity: Int, returnTypeClass: Class<ReturnType>): ReturnType? =
            fetch(
                Mangler.mangle(
                        getResourceEntryName(id),
                        Quantity.from(PluralRules.forLocale(LocaleUtil.current()), quantity)
                ),
                returnTypeClass
            )

    //------------------------------------- Resources getter -------------------------------------//

    private inline fun <reified T: Any> getArray(id: Int): Array<T>? {
        val getter = get(id, List::class.java)
        val newArr by lazy { ArrayList<T>() }
        getter?.forEach { value -> newArr.add(value as T) }
        return if (getter == null) null else newArr.toTypedArray()
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String = get(id, String::class.java) ?: super.getString(id)

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String =
            String.format(LocaleUtil.current()!!, getString(id), *formatArgs)

    @Throws(NotFoundException::class)
    override fun getStringArray(id: Int): Array<String> = getArray(id) ?: super.getStringArray(id)

    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int): String =
            get(id, quantity, String::class.java) ?: super.getQuantityString(id, quantity)

    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String =
            String.format(LocaleUtil.current()!!, getQuantityString(id, quantity), *formatArgs)

    @Throws(NotFoundException::class)
    override fun getBoolean(id: Int): Boolean =
            get(id, Boolean::class.java) ?: super.getBoolean(id)

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence =
            get(id, CharSequence::class.java) ?: super.getText(id)

    override fun getText(id: Int, def: CharSequence): CharSequence =
            get(id, CharSequence::class.java) ?: super.getText(id, def)

    @Throws(NotFoundException::class)
    override fun getTextArray(id: Int): Array<CharSequence> =
            getArray(id) ?: super.getTextArray(id)

    @Throws(NotFoundException::class)
    override fun getQuantityText(id: Int, quantity: Int): CharSequence =
            get(id, quantity, CharSequence::class.java) ?: super.getQuantityText(id, quantity)

    @Throws(NotFoundException::class)
    override fun getInteger(id: Int): Int =
            get(id, Double::class.java)?.toInt() ?: super.getInteger(id)

    @Throws(NotFoundException::class)
    override fun getIntArray(id: Int): IntArray {
        val response = getArray<Double>(id) ?: return super.getIntArray(id)
        val arr by lazy { IntArray(response.size) }

        response.forEachIndexed { index, value ->
            arr[index] = value.toInt()
        }

        return arr
    }

    @Throws(NotFoundException::class)
    override fun getDimension(id: Int): Float {
        val response = get(id, String::class.java)
        return if (response != null) {
            DimensionConverter.stringToDimension(response, displayMetrics)
        } else super.getDimension(id)
    }

    @Throws(NotFoundException::class)
    override fun getDimensionPixelSize(id: Int): Int {
        val response = get(id, String::class.java)
        return if (response != null) {
            DimensionConverter.stringToDimensionPixelSize(response, displayMetrics)
        } else super.getDimensionPixelSize(id)
    }

    //------------------------------------- Store interface --------------------------------------//

    /**
     * Delegate
     */
    override fun <Type> put(item: Item<Type>) = store.put(item)

    /**
     * Delegate
     */
    override fun <Type> putAll(items: List<Item<Type>>) = store.putAll(items)

    /**
     * Delegate
     */
    override fun clear() = store.clear()

}
