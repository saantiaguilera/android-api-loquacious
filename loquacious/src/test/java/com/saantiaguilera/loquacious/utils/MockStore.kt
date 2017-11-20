package com.saantiaguilera.loquacious.utils

import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.persistence.Store
import kotlin.reflect.KClass

/**
 * Created by saguilera on 11/19/17.
 */
class MockStore : Store {

    val list: ArrayList<Item<*>> by lazy { ArrayList<Item<*>>() }

    override fun <Type> fetch(key: String, klass: KClass<*>): Type? {
        val item = list[0]
        list.removeAt(0)
        @Suppress("UNCHECKED_CAST")
        return item.value as? Type
    }

    override fun <Type> put(key: String, item: Type, klass: KClass<*>) {
        list.add(Item(0, item))
    }

    override fun clear() {
        list.clear()
    }

    override fun accepts(element: String) = true

}