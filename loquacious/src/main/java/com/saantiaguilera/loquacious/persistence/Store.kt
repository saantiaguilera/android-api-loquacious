package com.saantiaguilera.loquacious.persistence

import android.support.annotation.CheckResult
import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/18/17.
 */
interface Store {
    /**
     * Fetch an element of type klass from the store, based on its key
     */
    @CheckResult
    fun <Type> fetch(key: String, klass: KClass<*>): Type?

    /**
     * Save an element in the store of type klass
     */
    fun <Type> put(key: String, item: Type, klass: KClass<*>)

    /**
     * Clear the store
     */
    fun clear()

    /**
     * Checks if the store accepts this type of resource. Resource will come in the form of
     * package:type/name
     *
     * For more information see:
     * https://developer.android.com/reference/android/content/res/Resources.html#getResourceName(int)
     * https://developer.android.com/reference/android/content/res/Resources.html#getResourceTypeName(int)
     *
     * Returns true if it can store this element, else false.
     */
    @CheckResult
    fun accepts(element: String): Boolean
}

/**
 * Since interfaces cant have reified parameters, I create the exposed methods others
 * should call.
 * Still, we will implement the above, having the class as the "reified parameter"
 */
inline fun <reified Type> Store.put(key: String, item: Type) = put(key, item, Type::class)