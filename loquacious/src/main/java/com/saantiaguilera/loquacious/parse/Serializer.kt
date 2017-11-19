package com.saantiaguilera.loquacious.parse

import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/18/17.
 */
interface Serializer {
    fun <Type> serialize(value: Type, klass: KClass<*>): String

    fun <Type> hydrate(string: String, klass: KClass<*>): Type
}

/**
 * Since interfaces cant have reified parameters, I create the exposed methods others
 * should call.
 * Still, we will implement the above, having the class as the "reified parameter"
 */
inline fun <reified T> Serializer.serialize(value: T): String = serialize(value, T::class)
inline fun <reified T> Serializer.hydrate(value: String): T = hydrate(value, T::class)