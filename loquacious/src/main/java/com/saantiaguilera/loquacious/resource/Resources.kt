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
    private inline fun <reified ReturnType> fetch(entryName: String): ReturnType? =
            store.fetch(entryName, ReturnType::class.java)?.value

    @CheckResult
    private inline fun <reified ReturnType> get(id: Int): ReturnType? =
            fetch(Mangler.mangle(getResourceEntryName(id)))

    @CheckResult
    private inline fun <reified ReturnType> get(id: Int, quantity: Int): ReturnType? =
            fetch(
                Mangler.mangle(
                        getResourceEntryName(id),
                        Quantity.from(PluralRules.forLocale(LocaleUtil.current()), quantity)
                )
            )

    //------------------------------------- Resources getter -------------------------------------//

    @Throws(NotFoundException::class)
    override fun getString(id: Int): String = get(id) ?: super.getString(id)

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String =
            String.format(LocaleUtil.current()!!, getString(id), *formatArgs)

    @Throws(NotFoundException::class)
    override fun getStringArray(id: Int): Array<String> =
            get<ArrayList<String>>(id)?.toTypedArray() ?: super.getStringArray(id)

    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int): String =
            get(id, quantity) ?: super.getQuantityString(id, quantity)

    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String =
            String.format(LocaleUtil.current()!!, getQuantityString(id, quantity), *formatArgs)

    @Throws(NotFoundException::class)
    override fun getBoolean(id: Int): Boolean =
            get(id) ?: super.getBoolean(id)

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence =
            get(id) ?: super.getText(id)

    override fun getText(id: Int, def: CharSequence): CharSequence =
            get(id) ?: super.getText(id, def)

    @Throws(NotFoundException::class)
    override fun getTextArray(id: Int): Array<CharSequence> =
            get<ArrayList<CharSequence>>(id)?.toTypedArray() ?: super.getTextArray(id)

    @Throws(NotFoundException::class)
    override fun getQuantityText(id: Int, quantity: Int): CharSequence =
            get(id, quantity) ?: super.getQuantityText(id, quantity)

    @Throws(NotFoundException::class)
    override fun getInteger(id: Int): Int =
            get<Double>(id)?.toInt() ?: super.getInteger(id)

    @Throws(NotFoundException::class)
    override fun getIntArray(id: Int): IntArray {
        val arr = get<ArrayList<Int>>(id) ?: return super.getIntArray(id)
        val unboxedArr by lazy { IntArray(arr.size) }
        arr.forEachIndexed { index, value -> unboxedArr[index] = value }
        return unboxedArr
    }

    @Throws(NotFoundException::class)
    override fun getDimension(id: Int): Float {
        val response = get<String>(id)
        return if (response != null) {
            DimensionConverter.stringToDimension(response, displayMetrics)
        } else super.getDimension(id)
    }

    @Throws(NotFoundException::class)
    override fun getDimensionPixelSize(id: Int): Int {
        val response = get<String>(id)
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
