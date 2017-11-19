package com.saantiaguilera.loquacious.model

/**
 * Created by saguilera on 11/18/17.
 */
data class Item<out StoreElement>(val key: Int, val value: StoreElement, val quantity: Quantity = Quantity.ONE)