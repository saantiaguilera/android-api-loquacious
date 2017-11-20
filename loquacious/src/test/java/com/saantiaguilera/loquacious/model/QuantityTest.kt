package com.saantiaguilera.loquacious.model

import com.ibm.icu.text.PluralRules

import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class QuantityTest {

    @Test
    fun test_QuantityGetsParsedCorrectly_FromPlurals() {
        val rules = PluralRules.forLocale(Locale.ENGLISH)
        Assert.assertEquals(Quantity.OTHER, Quantity.from(rules, 10))
    }

}
