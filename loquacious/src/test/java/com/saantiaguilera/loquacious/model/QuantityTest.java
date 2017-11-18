package com.saantiaguilera.loquacious.model;

import com.ibm.icu.text.PluralRules;
import com.saantiaguilera.loquacious.util.LocaleUtil;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class QuantityTest {

    @Test
    public void test_QuantityGetsParsedCorrectly_FromPlurals() {
        PluralRules rules = PluralRules.forLocale(LocaleUtil.setSystemLocale(RuntimeEnvironment.application));
        Assert.assertEquals(Quantity.OTHER, Quantity.from(rules, 10));
    }

}
