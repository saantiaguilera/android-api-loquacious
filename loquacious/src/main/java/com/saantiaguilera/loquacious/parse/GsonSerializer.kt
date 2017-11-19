package com.saantiaguilera.loquacious.parse

import com.google.gson.Gson
import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/19/17.
 */
@Suppress("UNCHECKED_CAST")
class GsonSerializer : Serializer {
    override fun <Type> hydrate(string: String, klass: KClass<*>): Type = Gson().fromJson(string, klass.java) as Type
    override fun <Type> serialize(value: Type, klass: KClass<*>): String = Gson().toJson(value, klass.java)
}