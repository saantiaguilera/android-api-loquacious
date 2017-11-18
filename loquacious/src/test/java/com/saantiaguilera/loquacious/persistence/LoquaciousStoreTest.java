package com.saantiaguilera.loquacious.persistence;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saantiaguilera.loquacious.Loquacious;
import com.saantiaguilera.loquacious.model.Item;
import com.saantiaguilera.loquacious.model.Quantity;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.util.LocaleUtil;
import com.saantiaguilera.loquacious.utils.MockDto;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class LoquaciousStoreTest {

    private LoquaciousStore store;

    @Before
    public void setUp() {
        Serializer serializer = new Serializer() {
            @NonNull
            @Override
            public <T> String serialize(@NonNull Item<T> item) {
                return new Gson().toJson(item);
            }

            @NonNull
            @Override
            public <T> Item<T> hidrate(@NonNull String string, Class<T> classType) {
                return new Gson().fromJson(string, new TypeToken<Item<T>>(){}.getType());
            }
        };
        Loquacious.initialize(RuntimeEnvironment.application, serializer);
        store = new LoquaciousStore(RuntimeEnvironment.application, serializer);
    }

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(Loquacious.class, "instance", null);
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", null);
    }

    @Test
    public void test_PutAnItem_EffectivelyStoresIt_InPhysicalMemmory() {
        Item<String> item = new Item<>("key-test", "value-test");
        store.put(item);

        Assert.assertEquals(item.getValue(), store.fetch(item.getKey(), String.class).getValue());
    }

    @Test
    public void test_PutAllItems_EffectivelyStoresThem_InPhysicalMemmory() {
        Item<Object> stringItem = new Item<Object>("string", "value");
        Item<Object> boolItem = new Item<Object>("boolean", true);
        Item<Object> intItem = new Item<Object>("integer", 149.0);
        store.putAll(Arrays.asList(stringItem, boolItem, intItem));

        Assert.assertEquals(stringItem.getValue(), store.fetch(stringItem.getKey(), stringItem.getValue().getClass()).getValue());
        Assert.assertEquals(boolItem.getValue(), store.fetch(boolItem.getKey(), boolItem.getValue().getClass()).getValue());
        Assert.assertEquals(intItem.getValue(), store.fetch(intItem.getKey(), intItem.getValue().getClass()).getValue());
    }

    @Test
    public void test_PuttingSameKeyItem_Overwrites_TheFirst() {
        Item<Double> item = new Item<>("key", 10.0);
        store.put(item);

        Assert.assertEquals(10.0, store.fetch(item.getKey(), Double.class).getValue());

        item = new Item<>("key", 40.0);
        store.put(item);

        Assert.assertEquals(40.0, store.fetch(item.getKey(), Double.class).getValue());
    }

    @Test
    public void test_Keys_DependsOn_Locale() {
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", Locale.UK);
        Item<Double> item = new Item<>("key", 10.0);
        store.put(item);
        Assert.assertEquals(10.0, store.fetch(item.getKey(), Double.class).getValue());

        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", Locale.JAPANESE);
        Assert.assertNull(store.fetch(item.getKey(), Double.class));

        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", Locale.UK);
        Assert.assertEquals(10.0, store.fetch(item.getKey(), Double.class).getValue());
    }

    @Test
    public void test_Retrieve_ABooleanItem() {
        Item<Boolean> item = new Item<>("key", true);
        store.put(item);
        Assert.assertEquals(item.getValue(), store.fetch(item.getKey(), Boolean.class).getValue());
    }

    @Test
    public void test_Retrieve_AStringItem() {
        Item<String> item = new Item<>("key", "test");
        store.put(item);
        Assert.assertEquals(item.getValue(), store.fetch(item.getKey(), String.class).getValue());
    }

    @Test
    public void test_Retrieve_APlural() {
        Item<String> item = new Item<>("key", Quantity.OTHER, "Other");
        store.put(item);
        Assert.assertEquals(item.getValue(), store.fetch(item.getKey(), String.class).getValue());
    }

    @Test
    public void test_Retrieve_ADTOItem_ParsesAsTree_BecauseOfJavaErasure() {
        MockDto dto = new MockDto();
        dto.price = 40;
        dto.itemName = "Blazer";
        dto.description = "A black blazer";

        Item<MockDto> item = new Item<>("key", Quantity.OTHER, dto);
        store.put(item);

        try {
            int pricePlusOne = store.fetch(item.getKey(), MockDto.class).getValue().price + 1;
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof ClassCastException);
        }
    }

    @Test
    public void test_ChangingLocale_ChangesKeys() {
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", Locale.UK);
        Item<Double> item = new Item<>("key", 10.0);
        store.put(item);
        Assert.assertEquals(10.0, store.fetch(item.getKey(), Double.class).getValue());

        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", Locale.JAPANESE);
        Assert.assertNull(store.fetch(item.getKey(), Double.class));
    }

    @Test
    public void test_Clear() {
        Item<Double> item = new Item<>("key", 10.0);
        store.put(item);
        Assert.assertEquals(10.0, store.fetch(item.getKey(), Double.class).getValue());

        store.clear();
        Assert.assertNull(store.fetch(item.getKey(), Double.class));
    }

}
