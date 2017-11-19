package com.saantiaguilera.loquacious

import android.content.Context
import android.content.res.Resources

/**
 * Created by saguilera on 11/19/17.
 */
class ContextWrapper(base: Context) : android.content.ContextWrapper(base) {

    override fun getResources(): Resources {
        return Loquacious.resources
    }

    companion object {

        internal fun wrap(base: Context) = ContextWrapper(base)

    }

}