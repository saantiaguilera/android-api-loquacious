package com.saantiaguilera.loquacious

import android.content.Context

/**
 * Created by saguilera on 11/19/17.
 */
class ContextWrapper(base: Context) : android.content.ContextWrapper(base) {

    override fun getResources() = Loquacious.resources

    companion object {

        internal fun wrap(base: Context) = ContextWrapper(base)

    }

}
