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
inline fun <reified Type> Serializer.serialize(value: Type): String = serialize(value, Type::class)
inline fun <reified Type> Serializer.hydrate(value: String): Type = hydrate(value, Type::class)