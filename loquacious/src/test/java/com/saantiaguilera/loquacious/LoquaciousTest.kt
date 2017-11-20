package com.saantiaguilera.loquacious

import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers
import java.util.*

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class LoquaciousTest {

    fun any() = Mockito.any(Locale::class.java) ?: Locale("en-US")

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "initialized", false)
    }

    @Test
    fun test_CantUseWithoutInit() {
        try {
            Loquacious.instance
            Assert.fail()
        } catch (ignored: Exception) {
        }

        try {
            Loquacious.resources
            Assert.fail()
        } catch (ignored: Exception) {
        }

    }

    @Test
    fun test_Initialization_CreatesInstance() {
        Loquacious.initialize(RuntimeEnvironment.application)
        Assert.assertNotNull(Loquacious.instance)
    }

    @Test
    fun test_Resources_Exist_IfInitialized() {
        Loquacious.initialize(RuntimeEnvironment.application)

        Assert.assertNotNull(Loquacious.resources)
    }

}
