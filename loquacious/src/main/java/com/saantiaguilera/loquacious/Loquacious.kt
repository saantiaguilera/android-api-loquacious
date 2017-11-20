package com.saantiaguilera.loquacious

import android.content.Context
import com.saantiaguilera.loquacious.persistence.Store
import com.saantiaguilera.loquacious.resource.Resources

/**
 * Entry point for the Loquacious library.
 */
class Loquacious private constructor(context: Context) {

    private val resources: Resources = Resources(context)

    companion object {

        private var initialized = false

        /**
         * Instance getter
         */
        lateinit var instance: Loquacious
            private set

        /**
         * Get the resources associated with loquacious.
         * If you have wrapped the context and attached it into the base,
         * then this resources are the same your application/activity/etc uses
         */
        val resources
            get() = Loquacious.instance.resources

        /**
         * Initialize the library
         */
        fun initialize(context: Context): Builder {
            if (initialized) {
                throw IllegalStateException("Already initialized!")
            }
            instance = Loquacious(context)
            initialized = true
            return Builder()
        }

        /**
         * Wraps the context in a new one. Useful if you want to use the remote resources
         * in XMLs or natively as activity.resources instead of Loquacious.resources
         */
        fun wrap(context: Context): Context {
            return ContextWrapper.wrap(context)
        }

    }

    class Builder internal constructor() {

        fun with(store: Store): Builder {
            resources.with(store)
            return this
        }

    }

}