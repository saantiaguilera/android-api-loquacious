package com.saantiaguilera.loquacious.resource

import android.content.SharedPreferences
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.persistence.LoquaciousStore
import com.saantiaguilera.loquacious.util.LocaleUtil
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers
import java.util.*


/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class ResourceTest {

    private var resources: Resources? = null

    @Before
    fun setUp() {
        Loquacious.initialize(RuntimeEnvironment.application)
        resources = Resources(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "initialized", false)
        resources = null
    }

    @Test
    fun test_PuttingA_SingleResource() {
        val store = ReflectionHelpers.getField<LoquaciousStore>(resources, "store")
        val sp = ReflectionHelpers.getField<SharedPreferences>(store, "sharedPreferences")

        val item = Item(android.R.string.cut, "test string")
        resources!!.put(item)

        Assert.assertEquals(1, sp.all.size)
    }

    @Test
    fun test_Putting_MultipleResources() {
        val store = ReflectionHelpers.getField<LoquaciousStore>(resources, "store")
        val sp = ReflectionHelpers.getField<SharedPreferences>(store, "sharedPreferences")

        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)

        Assert.assertEquals(2, sp.all.size)
    }

    @Test
    fun test_ClearingResources() {
        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)

        val store = ReflectionHelpers.getField<LoquaciousStore>(resources, "store")
        val sp = ReflectionHelpers.getField<SharedPreferences>(store, "sharedPreferences")

        Assert.assertEquals(2, sp.all.size)

        resources!!.clear()

        Assert.assertEquals(0, sp.all.size)
    }

    @Test
    fun test_Retrieving_String() {
        resources!!.put(Item(android.R.string.cut, "test string"))
        Assert.assertEquals("test string", resources!!.getString(android.R.string.cut))
    }

    @Test
    fun test_Retrieving_FormattedString() {
        resources!!.put(Item(android.R.string.cut, "test %1\$s"))

        Assert.assertEquals("test string", resources!!.getString(android.R.string.cut, "string"))
    }

    @Test
    fun test_Retrieving_StringArray() {
        val list = arrayOf("test string 1", "test string 2")
        resources!!.put(Item(android.R.string.cut, list))

        Assert.assertEquals("test string 1", resources!!.getStringArray(android.R.string.cut)[0])
        Assert.assertEquals("test string 2", resources!!.getStringArray(android.R.string.cut)[1])
    }

    @Test
    fun test_Retrieving_QuantityString() {
        resources!!.put(Item(android.R.string.cut, "test string", Quantity.ONE))

        // This is correct
        Assert.assertEquals("test string", resources!!.getQuantityString(android.R.string.cut, 1))
        // This shouldnt be
        try {
            resources!!.getQuantityString(android.R.string.cut, 5)
            Assert.fail()
        } catch (ignored: Exception) { }
    }

    @Test
    fun test_Retrieving_QuantityFormattedString() {
        resources!!.put(Item(android.R.string.cut, "test %1\$s", Quantity.ONE))

        // This is correct
        Assert.assertEquals("test string", resources!!.getQuantityString(android.R.string.cut, 1, "string"))
        // This shouldnt be
        try {
            resources!!.getQuantityString(android.R.string.cut, 5, "")
            Assert.fail()
        } catch (ignored: Exception) { }
    }

    @Test
    fun test_Retrieving_Boolean() {
        resources!!.put(Item(android.R.string.cut, true))

        Assert.assertTrue(resources!!.getBoolean(android.R.string.cut))
    }

    @Test
    fun test_Retrieving_Text() {
        resources!!.put(Item(android.R.string.cut, "test string"))

        Assert.assertEquals("test string", resources!!.getText(android.R.string.cut))
    }

    @Test
    fun test_Retrieving_RealText_WithDefault() {
        resources!!.put(Item(android.R.string.cut, "test string"))

        Assert.assertEquals("test string", resources!!.getText(android.R.string.cut, "default"))
    }

    @Test
    fun test_Retrieving_TextArray() {
        val list = arrayOf("test string 1", "test string 2")
        resources!!.put(Item(android.R.string.cut, list))

        Assert.assertEquals("test string 1", resources!!.getTextArray(android.R.string.cut)[0])
        Assert.assertEquals("test string 2", resources!!.getTextArray(android.R.string.cut)[1])
    }

    @Test
    fun test_Retrieving_QuantityText() {
        resources!!.put(Item(android.R.string.cut, "test string"))

        // This is correct
        Assert.assertEquals("test string", resources!!.getQuantityText(android.R.string.cut, 1))
        // This shouldnt be
        try {
            Assert.assertNull(resources!!.getQuantityText(android.R.string.cut, 5))
            Assert.fail()
        } catch (ex: Exception) {
            Assert.assertTrue(ex is UnsupportedOperationException)
        }

    }

    @Test
    fun test_Retrieving_Integer() {
        resources!!.put(Item(android.R.string.cut, 14))

        Assert.assertEquals(14, resources!!.getInteger(android.R.string.cut))
    }

    @Test
    fun test_Retrieving_IntegerArray() {
        resources!!.put(Item(android.R.string.cut, intArrayOf(1, 2, 3)))

        Assert.assertEquals(1, resources!!.getIntArray(android.R.string.cut)[0])
        Assert.assertEquals(2, resources!!.getIntArray(android.R.string.cut)[1])
        Assert.assertEquals(3, resources!!.getIntArray(android.R.string.cut)[2])
    }

    @Test
    fun test_Retrieving_Dimension() {
        resources!!.put(Item(android.R.string.cut, "22sp"))

        Assert.assertEquals(22.0f, resources!!.getDimension(android.R.string.cut))
    }

    @Test
    fun test_Retrieving_DimensionPixelSize() {
        resources!!.put(Item(android.R.string.cut, "22sp"))

        Assert.assertEquals(22, resources!!.getDimensionPixelSize(android.R.string.cut))
    }

}
