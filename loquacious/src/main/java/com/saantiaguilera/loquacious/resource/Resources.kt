package com.saantiaguilera.loquacious.resource

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.CheckResult
import android.support.annotation.RestrictTo
import com.ibm.icu.text.PluralRules
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.persistence.Store
import com.saantiaguilera.loquacious.persistence.put
import com.saantiaguilera.loquacious.util.Mangler.Companion.mangle
import java.util.*
import kotlin.reflect.KClass

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

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    var stores: List<Store> = ArrayList()
        private set

    fun with(store: Store) {
        if (stores.find { it.javaClass == store.javaClass } != null) {
            throw IllegalStateException("There's already a store of this type")
        }
        stores = stores.toMutableList().apply { add(store) }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
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
     * Base method, lets you retrieve a generic resource.
     *
     * @param id: Id of the element, per android resources (eg R.id.something)
     * @param quantity: Optional quantity if the resource is a plural
     * @param store: Specific store class to search at, else it will look in all the ones that
     * accept this kind of resource and return the first nonnull element fetched
     *
     * @return element if exists, null otherwise
     */
    @SuppressLint("CheckResult")
    @CheckResult
    inline fun <reified ReturnType> get(id: Int, quantity: Int = 1, store: KClass<out Store>? = null): ReturnType? {
        val resourceName = getResourceName(id)
        val quantityBoxed = Quantity.from(PluralRules.forLocale(currentLocale()), quantity)!!
        return stores
                .filter { if (store == null) true else store.java == it::class.java }
                .filter { it.accepts(resourceName) }
                .map { it.fetch<ReturnType>(mangle(resourceName, quantityBoxed), ReturnType::class) }
                .firstOrNull { it != null }
    }

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
     * Put whichever item you'd like into the resources. This item will be stored for the first store
     * that matches the item type, unless a store is specified, in which case (if it accepts the item)
     * it will be stored
     *
     * @param item: item to add
     * @param store: store class to add the item in, if not specified it will be the first one that matches
     * the item type from the currents. The specified store must accept this type of item if specified
     */
    @SuppressLint("CheckResult")
    inline fun <reified Type> put(item: Item<Type>, store: KClass<out Store>? = null) {
        val resourceName = getResourceName(item.key)
        stores.filter { if (store == null) true else it::class.java == store.java }
                .firstOrNull { it.accepts(resourceName) }!!
                .put(mangle(resourceName, item.quantity), item.value)
    }

    /**
     * Put whichever batch of items you'd like into the resources. These items will be stored for
     * the first store that matches the item resource, or the specified store
     * @param items: batch of items to store (can correspond to different stores, if store is not specified
     * @param store: force all items (that accept the store, else it wont happen anything) to be stored
     * in that specific store
     */
    inline fun <reified Type> putAll(items: List<Item<Type>>, store: KClass<out Store>? = null) {
        items.forEach { item -> put(item, store) }
    }

    /**
     * Clear all items.
     * @param store: If specified, it will only clear that store
     */
    @SuppressLint("CheckResult")
    fun clear(store: KClass<out Store>? = null) =
            stores.filter { if (store == null) true else it::class.java == store.java }
                    .forEach { it.clear() }

}
