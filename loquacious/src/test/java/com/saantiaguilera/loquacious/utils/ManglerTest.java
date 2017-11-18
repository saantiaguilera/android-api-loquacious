package com.saantiaguilera.loquacious.utils;

import com.saantiaguilera.loquacious.model.Quantity;
import com.saantiaguilera.loquacious.util.Mangler;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by saguilera on 11/18/17.
 */

public class ManglerTest {

    @Test
    public void test_Mangler_ContainsNameInfo() {
        Assert.assertTrue(Mangler.mangle("santi").contains("santi"));
    }

    @Test
    public void test_Mangler_ContainsQuantityInfo() {
        Assert.assertTrue(Mangler.mangle("santi", Quantity.MANY).contains("many"));
        Assert.assertTrue(Mangler.mangle("santi", Quantity.MANY).contains("santi"));
    }

    @Test
    public void test_Mangler_IsSameForSameData() {
        Assert.assertEquals(Mangler.mangle("test", Quantity.FEW), Mangler.mangle("test", Quantity.FEW));
    }

    @Test
    public void test_Mangler_IsDifferentForDifferentQuantityOrName() {
        Assert.assertFalse(Mangler.mangle("test", Quantity.FEW).contentEquals(Mangler.mangle("test", Quantity.MANY)));
        Assert.assertFalse(Mangler.mangle("test", Quantity.FEW).contentEquals(Mangler.mangle("test1", Quantity.FEW)));
    }

}
