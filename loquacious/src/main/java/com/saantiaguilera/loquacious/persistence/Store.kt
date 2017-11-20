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

        fun <Type> putAll(kv: List<Pair<String, Type>>, klass: KClass<*>)
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
inline fun <reified Type> Store.Commit.put(key: String, item: Type) = put(key, item, Type::class)
inline fun <reified Type> Store.Commit.putAll(kv: List<Pair<String, Type>>) = putAll(kv, Type::class)
