package com.saantiaguilera.loquacious.resource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.persistence.LoquaciousStore
import com.saantiaguilera.loquacious.util.LocaleUtil
import com.saantiaguilera.loquacious.util.Mangler
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
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
    private var store: LoquaciousStore? = null

    inline fun <reified T : Any> any(default: T) = Mockito.any(Item::class.java) ?: Item(0, default)

    @Before
    fun setUp() {
        val serializer = object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hydrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {}.type)
            }
        }
        Loquacious.initialize(RuntimeEnvironment.application, serializer)
        resources = Resources(RuntimeEnvironment.application, serializer)
        store = Mockito.spy(ReflectionHelpers.getField<Any>(resources!!, "store")) as LoquaciousStore
        ReflectionHelpers.setField(resources!!, "store", store)
    }

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
    }

    @Test
    fun test_PuttingA_SingleResource() {
        val item = Item(android.R.string.cut, "test string")
        resources!!.put(item)
        Mockito.verify<LoquaciousStore>(store).put(Mockito.anyString(), any(item))
    }

    @Test
    fun test_Putting_MultipleResources() {
        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)
        Mockito.verify<LoquaciousStore>(store).putAll(list.map { item ->
            Mangler.mangle(RuntimeEnvironment.application.resources.getResourceEntryName(item.key), item.quantity)
        }, list)
    }

    @Test
    fun test_ClearingResources() {
        val item1 = Item(android.R.string.cut, "test string")
        val item2 = Item(android.R.string.copy, "test string1")
        val list = ArrayList<Item<String>>()
        list.add(item1)
        list.add(item2)
        resources!!.putAll(list)
        Mockito.verify<LoquaciousStore>(store).putAll(list.map { item ->
            Mangler.mangle(RuntimeEnvironment.application.resources.getResourceEntryName(item.key), item.quantity)
        }, list)

        resources!!.clear()
        Mockito.verify<LoquaciousStore>(store).clear()
    }

    @Test
    fun test_Retrieving_String() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getString(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test string"))

        Assert.assertEquals("test string", resources.getString(1))
    }

    @Test
    fun test_Retrieving_FormattedString() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getString(Mockito.anyInt(), Mockito.anyString())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test %1\$s"))

        Assert.assertEquals("test string", resources.getString(1, "string"))
    }

    @Test
    fun test_Retrieving_StringArray() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getStringArray(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        val list = arrayOf("test string 1", "test string 2")
        resources.put(Item(android.R.string.cut, list))

        Assert.assertEquals("test string 1", resources.getStringArray(1)[0])
        Assert.assertEquals("test string 2", resources.getStringArray(1)[1])
    }

    @Test
    fun test_Retrieving_QuantityString() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getQuantityString(Mockito.anyInt(), Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test string", Quantity.ONE))

        // This is correct
        Assert.assertEquals("test string", resources.getQuantityString(1, 1))
        // This shouldnt be
        try {
            resources.getQuantityString(1, 5)
            Assert.fail()
        } catch (ignored: Exception) { }
    }

    @Test
    fun test_Retrieving_QuantityFormattedString() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getQuantityString(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test %1\$s", Quantity.ONE))

        // This is correct
        Assert.assertEquals("test string", resources.getQuantityString(1, 1, "string"))
        // This shouldnt be
        try {
            resources.getQuantityString(1, 5, "")
            Assert.fail()
        } catch (ignored: Exception) { }
    }

    @Test
    fun test_Retrieving_Boolean() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getBoolean(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, true))

        Assert.assertTrue(resources.getBoolean(1))
    }

    @Test
    fun test_Retrieving_Text() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getText(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test string"))

        Assert.assertEquals("test string", resources.getText(1))
    }

    @Test
    fun test_Retrieving_RealText_WithDefault() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getText(Mockito.anyInt(), Mockito.anyString())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test string"))

        Assert.assertEquals("test string", resources.getText(1, "default"))
    }

    @Test
    fun test_Retrieving_DefaultText_WithDefault() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getText(Mockito.anyInt(), Mockito.anyString())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        Assert.assertEquals("default", resources.getText(0, "default"))
    }

    @Test
    fun test_Retrieving_TextArray() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getTextArray(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        val list = arrayOf("test string 1", "test string 2")
        resources.put(Item(android.R.string.cut, list))

        Assert.assertEquals("test string 1", resources.getTextArray(1)[0])
        Assert.assertEquals("test string 2", resources.getTextArray(1)[1])
    }

    @Test
    fun test_Retrieving_QuantityText() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getQuantityText(Mockito.anyInt(), Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "test string"))

        // This is correct
        Assert.assertEquals("test string", resources.getQuantityText(1, 1))
        // This shouldnt be
        try {
            Assert.assertNull(resources.getQuantityText(1, 5))
            Assert.fail()
        } catch (ex: Exception) {
            Assert.assertTrue(ex is UnsupportedOperationException)
        }

    }

    @Test
    fun test_Retrieving_Integer() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getInteger(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, 14))

        Assert.assertEquals(14, resources.getInteger(1))
    }

    @Test
    fun test_Retrieving_IntegerArray() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getIntArray(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, intArrayOf(1, 2, 3)))

        Assert.assertEquals(1, resources.getIntArray(1)[0])
        Assert.assertEquals(2, resources.getIntArray(1)[1])
        Assert.assertEquals(3, resources.getIntArray(1)[2])
    }

    @Test
    fun test_Retrieving_Dimension() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getDimension(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "22sp"))

        Assert.assertEquals(22.0f, resources.getDimension(1))
    }

    @Test
    fun test_Retrieving_DimensionPixelSize() {
        val resources = Mockito.spy(this.resources!!)
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("cut").`when`(resources).getResourceEntryName(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).getDimensionPixelSize(Mockito.anyInt())
        Mockito.doCallRealMethod().`when`(resources).put(any(1))

        resources.put(Item(android.R.string.cut, "22sp"))

        Assert.assertEquals(22, resources.getDimensionPixelSize(1))
    }

}
