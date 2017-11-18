package com.saantiaguilera.loquacious.utils

import com.saantiaguilera.loquacious.util.LocaleUtil

import org.junit.Assert

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class LocaleUtilTest {

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
    }

    @Test
    fun test_ItCant_Instantiate() {
        try {
            ReflectionHelpers.callConstructor(LocaleUtil::class.java)
            Assert.fail()
        } catch (ignored: Exception) {
        }

    }

    @Test
    fun test_SetSystemLocale_SetsCurrent() {
        Assert.assertNull(LocaleUtil.current())

        LocaleUtil.setSystemLocale(RuntimeEnvironment.application)

        Assert.assertNotNull(LocaleUtil.current())
    }

}
