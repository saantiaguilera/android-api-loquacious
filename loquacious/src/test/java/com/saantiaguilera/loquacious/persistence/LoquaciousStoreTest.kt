package com.saantiaguilera.loquacious.persistence

import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.model.Quantity
import com.saantiaguilera.loquacious.util.LocaleUtil
import com.saantiaguilera.loquacious.utils.MockDto
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
class LoquaciousStoreTest {

    private var store: LoquaciousStore? = null

    @Before
    fun setUp() {
        Loquacious.initialize(RuntimeEnvironment.application)
        store = LoquaciousStore(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        ReflectionHelpers.setStaticField(Loquacious::class.java, "instance", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", null)
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "initialized", false)
    }

    @Test
    fun test_PutAnItem_EffectivelyStoresIt_InPhysicalMemmory() {
        val item = Item(android.R.string.copy, "value-test")
        store!!.put("copy", item.value)

        Assert.assertEquals(item.value, store!!.fetch("copy", String::class))
    }

    @Test
    fun test_PutAllItems_EffectivelyStoresThem_InPhysicalMemmory() {
        val stringItem = Item<Any>(android.R.string.copy, "value")
        val boolItem = Item<Any>(android.R.string.copyUrl, true)
        val intItem = Item<Any>(android.R.string.cut, 149.0)
        val items = Arrays.asList(stringItem, boolItem, intItem)
        store!!.putAll(items.map { Pair(RuntimeEnvironment.application.resources.getResourceEntryName(it.key), it.value) })

        Assert.assertEquals(stringItem.value, store!!.fetch("copy", stringItem.value::class))
        Assert.assertEquals(boolItem.value, store!!.fetch("copyUrl", boolItem.value::class))
        Assert.assertEquals(intItem.value, store!!.fetch("cut", intItem.value::class))
    }

    @Test
    fun test_PuttingSameKeyItem_Overwrites_TheFirst() {
        var item = Item(android.R.string.cut, 10.0)
        store!!.put("cut", item.value)

        Assert.assertEquals(10.0, store!!.fetch("cut", Double::class)!!, 0.0)

        item = Item(android.R.string.cut, 40.0)
        store!!.put("cut", item.value)

        Assert.assertEquals(40.0, store!!.fetch("cut", Double::class)!!, 0.0)
    }

    @Test
    fun test_Keys_DependsOn_Locale() {
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.UK)
        val item = Item(android.R.string.cut, 10.0)
        store!!.put("cut", item.value)
        Assert.assertEquals(10.0, store!!.fetch("cut", Double::class)!!, 0.0)

        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.JAPANESE)
        Assert.assertNull(store!!.fetch("cut", Double::class))

        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.UK)
        Assert.assertEquals(10.0, store!!.fetch("cut", Double::class)!!, 0.0)
    }

    @Test
    fun test_Retrieve_ABooleanItem() {
        val item = Item(android.R.string.cut, true)
        store!!.put("cut", item.value)
        Assert.assertEquals(item.value, store!!.fetch("cut", Boolean::class)!!)
    }

    @Test
    fun test_Retrieve_AStringItem() {
        val item = Item(android.R.string.cut, "test")
        store!!.put("cut", item.value)
        Assert.assertEquals(item.value, store!!.fetch("cut", String::class)!!)
    }

    @Test
    fun test_Retrieve_APlural() {
        val item = Item(android.R.string.cut, Quantity.OTHER, Quantity.OTHER)
        store!!.put("cut", item.value)
        Assert.assertEquals("OTHER", store!!.fetch("cut", String::class)!!)
    }

    @Test
    fun test_Retrieve_ADTOItem() {
        val dto = MockDto()
        dto.price = 40
        dto.itemName = "Blazer"
        dto.description = "A black blazer"

        val item = Item(android.R.string.cut, dto, Quantity.OTHER)
        store!!.put("cut", item.value)

        Assert.assertEquals(item.value.description, store!!.fetch<MockDto>("cut", MockDto::class)!!.description)
        Assert.assertEquals(item.value.itemName, store!!.fetch<MockDto>("cut", MockDto::class)!!.itemName)
        Assert.assertEquals(item.value.price, store!!.fetch<MockDto>("cut", MockDto::class)!!.price)
    }

    @Test
    fun test_ChangingLocale_ChangesKeys() {
        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.UK)
        val item = Item(android.R.string.cut, 10.0)
        store!!.put("cut", item.value)
        Assert.assertEquals(10.0, store!!.fetch("cut", Double::class)!!, 0.0)

        ReflectionHelpers.setStaticField(LocaleUtil::class.java, "current", Locale.JAPANESE)
        Assert.assertNull(store!!.fetch("cut", Double::class))
    }

    @Test
    fun test_Clear() {
        val item = Item(android.R.string.cut, 10.0)
        store!!.put("cut", item.value)
        Assert.assertEquals(10.0, store!!.fetch("cut", Double::class)!!, 0.0)

        store!!.clear()
        Assert.assertNull(store!!.fetch("cut", Double::class))
    }

}
