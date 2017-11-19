package com.saantiaguilera.loquacious.persistence

import android.support.annotation.CheckResult

import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/18/17.
 */
interface Store {

    interface Fetch {
        @CheckResult
        fun <Type> fetch(name: String, klass: KClass<*>): Type?
    }

    interface Commit {
        fun <Type> put(key: String, item: Type, klass: KClass<*>)

        fun <Type> putAll(key: List<String>, items: List<Type>, klass: KClass<*>)
    }

    interface Clear {
        fun clear()
    }

}

/**
 * Since interfaces cant have reified parameters, I create the exposed methods others
 * should call.
 * Still, we will implement the above, having the class as the "reified parameter"
 */
inline fun <reified T> Store.Commit.put(key: String, item: T) = put(key, item, T::class)
inline fun <reified T> Store.Commit.putAll(keys: List<String>, items: List<T>) = putAll(keys, items, T::class)
