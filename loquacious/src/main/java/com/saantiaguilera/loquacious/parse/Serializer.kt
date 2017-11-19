package com.saantiaguilera.loquacious.parse

import com.saantiaguilera.loquacious.model.Item

/**
 * Created by saguilera on 11/18/17.
 */
interface Serializer {
    fun <Type> serialize(item: Item<Type>): String

    fun <Type> hydrate(string: String, classType: Class<Type>): Item<Type>
}
