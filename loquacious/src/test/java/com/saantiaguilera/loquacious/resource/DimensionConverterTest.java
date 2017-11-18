package com.saantiaguilera.loquacious.resource;

import android.support.annotation.Dimension;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class DimensionConverterTest {

    @Test
    public void test_StringTo_PixelSize() {
        int px = DimensionConverter.stringToDimensionPixelSize("22dp", RuntimeEnvironment.application.getResources().getDisplayMetrics());
        Assert.assertEquals(22, px);
    }

    @Test
    public void test_StringTo_Dimension() {
        float dim = DimensionConverter.stringToDimension("22dp", RuntimeEnvironment.application.getResources().getDisplayMetrics());
        Assert.assertEquals(22.0f, dim);
    }

}
