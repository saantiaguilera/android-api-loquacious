package com.saantiaguilera.loquacious.utils

import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.util.Mangler

import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class ManglerTest {

    @Test
    fun test_Manger_CantBeInstantiated() {
        try {
            ReflectionHelpers.callConstructor(Mangler::class.java)
            Assert.fail()
        } catch (ignored: Exception) {}
    }

    @Test
    fun test_Mangler_ContainsNameInfo() {
        Assert.assertTrue(Mangler.mangle("santi").contains("santi"))
    }

    @Test
    fun test_Mangler_ContainsQuantityInfo() {
        Assert.assertTrue(Mangler.mangle("santi", Quantity.MANY).contains("many"))
        Assert.assertTrue(Mangler.mangle("santi", Quantity.MANY).contains("santi"))
    }

    @Test
    fun test_Mangler_IsSameForSameData() {
        Assert.assertEquals(Mangler.mangle("test", Quantity.FEW), Mangler.mangle("test", Quantity.FEW))
    }

    @Test
    fun test_Mangler_IsDifferentForDifferentQuantityOrName() {
        Assert.assertFalse(Mangler.mangle("test", Quantity.FEW).contentEquals(Mangler.mangle("test", Quantity.MANY)))
        Assert.assertFalse(Mangler.mangle("test", Quantity.FEW).contentEquals(Mangler.mangle("test1", Quantity.FEW)))
    }

}
