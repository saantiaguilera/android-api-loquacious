package com.saantiaguilera.loquacious.resource

import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.persistence.Store
import com.saantiaguilera.loquacious.utils.MockStore
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
        resources!!.with(MockStore())
    }

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(Loquacious::class.java, "initialized", false)
        resources = null
    }

    @Test
    fun test_PuttingA_SingleResource() {
        val store: MockStore = resources!!.stores[0] as MockStore

        val item = Item(android.R.string.cut, "test string")
        resources!!.put(item)

        Assert.assertEquals(1, store.list.size)
    }

    @Test
    fun test_Putting_MultipleResources() {
        val store: MockStore = resources!!.stores[0] as MockStore

        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)

        Assert.assertEquals(2, store.list.size)
    }

    @Test
    fun test_ClearingResources() {
        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)

        val store: MockStore = resources!!.stores[0] as MockStore

        Assert.assertEquals(2, store.list.size)

        resources!!.clear()

        Assert.assertEquals(0, store.list.size)
    }

    @Test
    fun test_Clearing_StringResources() {
        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)

        val store: MockStore = resources!!.stores[0] as MockStore

        Assert.assertEquals(2, store.list.size)

        resources!!.clear("string")

        Assert.assertEquals(0, store.list.size)
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
        val list = ArrayList<String>()
        list.add("test string 1")
        list.add("test string 2")
        resources!!.put(Item(android.R.string.cut, list))

        val arr: Array<String> = resources!!.getStringArray(android.R.string.cut)

        Assert.assertEquals("test string 1", arr[0])
        Assert.assertEquals("test string 2", arr[1])
    }

    @Test
    fun test_Retrieving_QuantityString() {
        resources!!.put(Item(android.R.string.cut, "test string", Quantity.ONE))

        Assert.assertEquals("test string", resources!!.getQuantityString(android.R.string.cut, 1))
    }

    @Test
    fun test_Retrieving_QuantityFormattedString() {
        resources!!.put(Item(android.R.string.cut, "test %1\$s", Quantity.ONE))

        Assert.assertEquals("test string", resources!!.getQuantityString(android.R.string.cut, 1, "string"))
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
        val list = ArrayList<String>()
        list.add("test string 1")
        list.add("test string 2")
        resources!!.put(Item(android.R.string.cut, list))

        val arr: Array<CharSequence> = resources!!.getTextArray(android.R.string.cut)
        Assert.assertEquals("test string 1", arr[0])
        Assert.assertEquals("test string 2", arr[1])
    }

    @Test
    fun test_Retrieving_QuantityText() {
        resources!!.put(Item(android.R.string.cut, "test string", Quantity.ONE))

        Assert.assertEquals("test string", resources!!.getQuantityText(android.R.string.cut, 1))
    }

    @Test
    fun test_Retrieving_Integer() {
        resources!!.put(Item(android.R.string.cut, 14.0))

        Assert.assertEquals(14, resources!!.getInteger(android.R.string.cut))
    }

    @Test
    fun test_Retrieving_IntegerArray() {
        val list = ArrayList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        resources!!.put(Item(android.R.string.cut, list))

        val arr: IntArray = resources!!.getIntArray(android.R.string.cut)
        Assert.assertEquals(1, arr[0])
        Assert.assertEquals(2, arr[1])
        Assert.assertEquals(3, arr[2])
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
