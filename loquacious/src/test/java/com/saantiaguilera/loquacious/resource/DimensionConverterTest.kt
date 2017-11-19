package com.saantiaguilera.loquacious.resource

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class DimensionConverterTest {

    @Test
    fun test_StringTo_PixelSize() {
        val px = DimensionConverter.stringToDimensionPixelSize("22dp", RuntimeEnvironment.application.resources.displayMetrics)
        Assert.assertEquals(22, px)
    }

    @Test
    fun test_StringTo_Dimension() {
        val dim = DimensionConverter.stringToDimension("22dp", RuntimeEnvironment.application.resources.displayMetrics)
        Assert.assertEquals(22.0f, dim)
    }

}
