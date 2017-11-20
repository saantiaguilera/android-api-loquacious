package com.saantiaguilera.loquacious.resource

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.CheckResult
import com.ibm.icu.text.PluralRules
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.persistence.Store
import com.saantiaguilera.loquacious.persistence.put
import com.saantiaguilera.loquacious.persistence.putAll
import com.saantiaguilera.loquacious.util.Mangler.Companion.mangle
import java.util.*

/**
 * Resources class for getting resources.
 */
@Suppress("DEPRECATION")
class Resources(context: Context) :
        android.content.res.Resources(
                context.assets,
                context.resources.displayMetrics,
                context.resources.configuration
        ) {

    lateinit var store: Store

    fun with(store: Store) {
        this.store = store
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.N)
    fun currentLocale(): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return configuration.locales.get(0)
        }
        return configuration.locale
    }

    //------------------------------------ Generic Getters ---------------------------------------//

    /**
     * Although we support a normal resource. We also provide this three methods
     * for getting whatever localized resource you have.
     * Lets say you want to save user data based on locales? No problem, simply create an Item<User>
     * and according to the locale save it. You can query it back with whichever of these 3 methods
     * fits you finest
     */

    /**
     * Get a generic resource based on the saved entry name
     * Careful this entryName is probably mangled.
     * This method should be the least of the options you should pick
     */
    @CheckResult
    inline fun <reified ReturnType> get(entryName: String): ReturnType? =
            store.fetch(entryName, ReturnType::class)

    /**
     * Get a generic resource based on its ResId
     */
    @CheckResult
    inline fun <reified ReturnType> get(id: Int): ReturnType? =
            get(mangle(getResourceEntryName(id)))

    /**
     * Get a generic resource based on its ResId + quantity plural
     */
    @CheckResult
    inline fun <reified ReturnType> get(id: Int, quantity: Int): ReturnType? =
            get(mangle(
                        getResourceEntryName(id),
                        Quantity.from(PluralRules.forLocale(currentLocale()), quantity)!!
            ))

    //------------------------------------- Resources getter -------------------------------------//

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getString(id: Int): String = get(id) ?: super.getString(id)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String =
            String.format(currentLocale(), getString(id), *formatArgs)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getStringArray(id: Int): Array<String> =
            get<ArrayList<String>>(id)?.toTypedArray() ?: super.getStringArray(id)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int): String =
            get(id, quantity) ?: super.getQuantityString(id, quantity)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any): String =
            String.format(currentLocale(), getQuantityString(id, quantity), *formatArgs)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getBoolean(id: Int): Boolean =
            get(id) ?: super.getBoolean(id)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence =
            get<String>(id) ?: super.getText(id)

    /**
     * Inherits javadoc
     */
    override fun getText(id: Int, def: CharSequence): CharSequence =
            get<String>(id) ?: super.getText(id, def)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getTextArray(id: Int): Array<CharSequence> =
            get<ArrayList<CharSequence>>(id)?.toTypedArray() ?: super.getTextArray(id)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getQuantityText(id: Int, quantity: Int): CharSequence =
            get<String>(id, quantity) ?: super.getQuantityText(id, quantity)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getInteger(id: Int): Int =
            get<Double>(id)?.toInt() ?: super.getInteger(id)

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getIntArray(id: Int): IntArray {
        val arr = get<ArrayList<Int>>(id) ?: return super.getIntArray(id)
        val unboxedArr by lazy { IntArray(arr.size) }
        arr.forEachIndexed { index, value -> unboxedArr[index] = value }
        return unboxedArr
    }

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getDimension(id: Int): Float {
        val response = get<String>(id)
        return if (response != null) {
            DimensionConverter.stringToDimension(response, displayMetrics)
        } else super.getDimension(id)
    }

    /**
     * Inherits javadoc
     */
    @Throws(NotFoundException::class)
    override fun getDimensionPixelSize(id: Int): Int {
        val response = get<String>(id)
        return if (response != null) {
            DimensionConverter.stringToDimensionPixelSize(response, displayMetrics)
        } else super.getDimensionPixelSize(id)
    }

    //------------------------------------- Store interface --------------------------------------//

    /**
     * Put whichever item you'd like into the resources. This item will be stored for the current
     * locale
     */
    inline fun <reified Type> put(item: Item<Type>) =
            store.put(mangle(getResourceEntryName(item.key), item.quantity), item.value)

    /**
     * Put whichever batch of items you'd like into the resources. These items will be stored for the
     * current locale
     */
    inline fun <reified Type> putAll(items: List<Item<Type>>) =
            store.putAll(items.map { Pair(mangle(getResourceEntryName(it.key), it.quantity), it.value) })

    /**
     * Clear all the items stored. This applies for all the locales (not just the current)
     */
    fun clear() = store.clear()

}
