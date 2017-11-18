package com.saantiaguilera.loquacious.model

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class ItemTest {

    @Test
    fun test_SingleItemHasMangledKey() {
        val item = Item("test", "value")
        Assert.assertNotEquals("test", item.key)
        Assert.assertEquals("value", item.value)
    }

    @Test
    fun test_QuantityItemHasMangledKey() {
        val item = Item("test", Quantity.ZERO, "value")
        Assert.assertNotEquals("test", item.key)
        Assert.assertNotEquals(Quantity.ZERO, item.key)
        Assert.assertEquals("value", item.value)
    }

}
