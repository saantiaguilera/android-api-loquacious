package com.saantiaguilera.loquacious.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class ItemTest {

    @Test
    public void test_SingleItemHasMangledKey() {
        Item<String> item = new Item<>("test", "value");
        Assert.assertNotEquals("test", item.getKey());
        Assert.assertEquals("value", item.getValue());
    }

    @Test
    public void test_QuantityItemHasMangledKey() {
        Item<String> item = new Item<>("test", Quantity.ZERO, "value");
        Assert.assertNotEquals("test", item.getKey());
        Assert.assertNotEquals(Quantity.ZERO, item.getKey());
        Assert.assertEquals("value", item.getValue());
    }

}
