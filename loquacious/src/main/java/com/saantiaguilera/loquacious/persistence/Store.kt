package com.saantiaguilera.loquacious.persistence

import android.support.annotation.CheckResult

import com.saantiaguilera.loquacious.model.Item

/**
 * Created by saguilera on 11/18/17.
 */
interface Store {

    interface Fetch {
        @CheckResult
        fun <Type> fetch(name: String, typeClass: Class<Type>): Item<Type>?
    }

    interface Commit {
        fun <Type> put(item: Item<Type>)

        fun <Type> putAll(items: List<Item<Type>>)
    }

    interface Clear {
        fun clear()
    }

}
