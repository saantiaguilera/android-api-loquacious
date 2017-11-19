package com.saantiaguilera.loquacious.persistence

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil
import com.saantiaguilera.loquacious.utils.MockDto

import org.junit.Assert

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.util.ReflectionHelpers

import java.util.Arrays
import java.util.Locale

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner::class)
class LoquaciousStoreTest {

    private var store: LoquaciousStore? = null

    @Before
    fun setUp() {
        val serializer = object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hidrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {}.type)
            }
        }
        Loquacious.initialize(RuntimeEnvironment.application, serializer)
        store = LoquaciousStore(RuntimeEnvironment.application, serializer)
    }

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "loquacious", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
    }

    @Test
    fun test_PutAnItem_EffectivelyStoresIt_InPhysicalMemmory() {
        val item = Item("key-test", "value-test")
        store!!.put(item)

        Assert.assertEquals(item.value, store!!.fetch(item.key, String::class.java)!!.value)
    }

    @Test
    fun test_PutAllItems_EffectivelyStoresThem_InPhysicalMemmory() {
        val stringItem = Item<Any>("string", "value")
        val boolItem = Item<Any>("boolean", true)
        val intItem = Item<Any>("integer", 149.0)
        store!!.putAll(Arrays.asList(stringItem, boolItem, intItem))

        Assert.assertEquals(stringItem.value, store!!.fetch(stringItem.key, stringItem.value.javaClass)!!.value)
        Assert.assertEquals(boolItem.value, store!!.fetch(boolItem.key, boolItem.value.javaClass)!!.value)
        Assert.assertEquals(intItem.value, store!!.fetch(intItem.key, intItem.value.javaClass)!!.value)
    }

    @Test
    fun test_PuttingSameKeyItem_Overwrites_TheFirst() {
        var item = Item("key", 10.0)
        store!!.put(item)

        Assert.assertEquals(10.0, store!!.fetch(item.key, Double::class.java)!!.value, 0.0)

        item = Item("key", 40.0)
        store!!.put(item)

        Assert.assertEquals(40.0, store!!.fetch(item.key, Double::class.java)!!.value, 0.0)
    }

    @Test
    fun test_Keys_DependsOn_Locale() {
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.UK)
        val item = Item("key", 10.0)
        store!!.put(item)
        Assert.assertEquals(10.0, store!!.fetch(item.key, Double::class.java)!!.value, 0.0)

        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.JAPANESE)
        Assert.assertNull(store!!.fetch(item.key, Double::class.java))

        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.UK)
        Assert.assertEquals(10.0, store!!.fetch(item.key, Double::class.java)!!.value, 0.0)
    }

    @Test
    fun test_Retrieve_ABooleanItem() {
        val item = Item("key", true)
        store!!.put(item)
        Assert.assertEquals(item.value, store!!.fetch(item.key, Boolean::class.java)!!.value)
    }

    @Test
    fun test_Retrieve_AStringItem() {
        val item = Item("key", "test")
        store!!.put(item)
        Assert.assertEquals(item.value, store!!.fetch(item.key, String::class.java)!!.value)
    }

    @Test
    fun test_Retrieve_APlural() {
        val item = Item("key", Quantity.OTHER, "Other")
        store!!.put(item)
        Assert.assertEquals(item.value, store!!.fetch(item.key, String::class.java)!!.value)
    }

    @Test
    fun test_Retrieve_ADTOItem_ParsesAsTree_BecauseOfJavaErasure() {
        val dto = MockDto()
        dto.price = 40
        dto.itemName = "Blazer"
        dto.description = "A black blazer"

        val item = Item("key", Quantity.OTHER, dto)
        store!!.put(item)

        try {
            store!!.fetch(item.key, MockDto::class.java)!!.value.price!! + 1
            Assert.fail()
        } catch (ex: Exception) {
            Assert.assertTrue(ex is ClassCastException)
        }

    }

    @Test
    fun test_ChangingLocale_ChangesKeys() {
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.UK)
        val item = Item("key", 10.0)
        store!!.put(item)
        Assert.assertEquals(10.0, store!!.fetch(item.key, Double::class.java)!!.value, 0.0)

        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.JAPANESE)
        Assert.assertNull(store!!.fetch(item.key, Double::class.java))
    }

    @Test
    fun test_Clear() {
        val item = Item("key", 10.0)
        store!!.put(item)
        Assert.assertEquals(10.0, store!!.fetch(item.key, Double::class.java)!!.value, 0.0)

        store!!.clear()
        Assert.assertNull(store!!.fetch(item.key, Double::class.java))
    }

}
