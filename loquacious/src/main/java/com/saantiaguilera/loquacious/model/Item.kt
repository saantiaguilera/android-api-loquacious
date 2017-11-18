package com.saantiaguilera.loquacious.model

import com.saantiaguilera.loquacious.util.Mangler

/**
 * Created by saguilera on 11/18/17.
 */
class Item<StoreElement> {

    val key: String

    val value: StoreElement

    constructor(key: String, value: StoreElement) {
        this.key = Mangler.mangle(key)
        this.value = value
    }

    constructor(key: String, quantity: Quantity, value: StoreElement) {
        this.key = Mangler.mangle(key, quantity)
        this.value = value
    }

}
