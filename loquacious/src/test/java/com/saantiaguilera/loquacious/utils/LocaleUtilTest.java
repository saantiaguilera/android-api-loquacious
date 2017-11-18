package com.saantiaguilera.loquacious.utils;

import com.saantiaguilera.loquacious.util.LocaleUtil;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class LocaleUtilTest {

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", null);
    }

    @Test
    public void test_ItCant_Instantiate() {
        try {
            ReflectionHelpers.callConstructor(LocaleUtil.class);
            Assert.fail();
        } catch (Exception ignored) {}
    }

    @Test
    public void test_SetSystemLocale_SetsCurrent() {
        Assert.assertNull(LocaleUtil.current());

        LocaleUtil.setSystemLocale(RuntimeEnvironment.application);

        Assert.assertNotNull(LocaleUtil.current());
    }

}
