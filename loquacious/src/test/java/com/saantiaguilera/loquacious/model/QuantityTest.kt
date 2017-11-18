package com.saantiaguilera.loquacious.model

import com.ibm.icu.text.PluralRules
import com.saantiaguilera.loquacious.util.LocaleUtil

import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class QuantityTest {

    @Test
    fun test_QuantityGetsParsedCorrectly_FromPlurals() {
        val rules = PluralRules.forLocale(LocaleUtil.setSystemLocale(RuntimeEnvironment.application))
        Assert.assertEquals(Quantity.OTHER, Quantity.from(rules, 10))
    }

}
