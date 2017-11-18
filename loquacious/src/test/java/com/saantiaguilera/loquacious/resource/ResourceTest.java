package com.saantiaguilera.loquacious.resource;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saantiaguilera.loquacious.Loquacious;
import com.saantiaguilera.loquacious.model.Item;
import com.saantiaguilera.loquacious.model.Quantity;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.persistence.LoquaciousStore;
import com.saantiaguilera.loquacious.util.LocaleUtil;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class ResourceTest {

    private Resources resources;
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
        resources = new Resources(RuntimeEnvironment.application, serializer);
        store = (LoquaciousStore) Mockito.spy(ReflectionHelpers.getField(resources, "store"));
        ReflectionHelpers.setField(resources, "store", store);
    }

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(Loquacious.class, "instance", null);
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", null);
    }

    @Test
    public void test_PuttingA_SingleResource() {
        Item<String> item = new Item<>("R.string.test", "test string");
        resources.put(item);
        Mockito.verify(store).put(item);
    }

    @Test
    public void test_Putting_MultipleResources() {
        Item<String> item1 = new Item<>("R.string.test", "test string");
        Item<String> item2 = new Item<>("R.string.test1", "test string1");
        List<Item<String>> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        resources.putAll(list);
        Mockito.verify(store).putAll(list);
    }

    @Test
    public void test_ClearingResources() {
        Item<String> item1 = new Item<>("R.string.test", "test string");
        Item<String> item2 = new Item<>("R.string.test1", "test string1");
        List<Item<String>> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        resources.putAll(list);
        Mockito.verify(store).putAll(list);

        resources.clear();
        Mockito.verify(store).clear();
    }

    @Test
    public void test_Retrieving_String() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getString(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", "test string"));

        Assert.assertEquals("test string", resources.getString(1));
    }

    @Test
    public void test_Retrieving_FormattedString() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", "test %1$s"));

        Assert.assertEquals("test string", resources.getString(1, "string"));
    }

    @Test
    public void test_Retrieving_StringArray() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getStringArray(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        String[] list = new String[] { "test string 1", "test string 2" };
        resources.put(new Item<>("R.string.test", list));

        Assert.assertEquals("test string 1", resources.getStringArray(1)[0]);
        Assert.assertEquals("test string 2", resources.getStringArray(1)[1]);
    }

    @Test
    public void test_Retrieving_QuantityString() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getQuantityString(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", Quantity.ONE, "test string"));

        // This is correct
        Assert.assertEquals("test string", resources.getQuantityString(1, 1));
        // This shouldnt be
        Assert.assertNull(resources.getQuantityString(1, 5));
    }

    @Test
    public void test_Retrieving_QuantityFormattedString() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getQuantityString(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", Quantity.ONE, "test %1$s"));

        // This is correct
        Assert.assertEquals("test string", resources.getQuantityString(1, 1, "string"));
        // This shouldnt be
        Assert.assertNull(resources.getQuantityString(1, 5));
    }

    @Test
    public void test_Retrieving_Boolean() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getBoolean(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", true));

        Assert.assertTrue(resources.getBoolean(1));
    }

    @Test
    public void test_Retrieving_Text() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getText(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", "test string"));

        Assert.assertEquals("test string", resources.getText(1));
    }

    @Test
    public void test_Retrieving_RealText_WithDefault() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getText(Mockito.anyInt(), Mockito.anyString());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", "test string"));

        Assert.assertEquals("test string", resources.getText(1, "default"));
    }

    @Test
    public void test_Retrieving_DefaultText_WithDefault() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getText(Mockito.anyInt(), Mockito.anyString());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        Assert.assertEquals("default", resources.getText(0, "default"));
    }

    @Test
    public void test_Retrieving_TextArray() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getTextArray(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        String[] list = new String[] { "test string 1", "test string 2" };
        resources.put(new Item<>("R.string.test", list));

        Assert.assertEquals("test string 1", resources.getTextArray(1)[0]);
        Assert.assertEquals("test string 2", resources.getTextArray(1)[1]);
    }

    @Test
    public void test_Retrieving_QuantityText() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getQuantityText(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", Quantity.ONE, "test string"));

        // This is correct
        Assert.assertEquals("test string", resources.getQuantityText(1, 1));
        // This shouldnt be
        try {
            Assert.assertNull(resources.getQuantityText(1, 5));
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void test_Retrieving_Integer() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getInteger(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", 14));

        Assert.assertEquals(14, resources.getInteger(1));
    }

    @Test
    public void test_Retrieving_IntegerArray() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getIntArray(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", new int[] { 1, 2, 3 }));

        Assert.assertEquals(1, resources.getIntArray(1)[0]);
        Assert.assertEquals(2, resources.getIntArray(1)[1]);
        Assert.assertEquals(3, resources.getIntArray(1)[2]);
    }

    @Test
    public void test_Retrieving_Dimension() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getDimension(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", "22sp"));

        Assert.assertEquals(22.0f, resources.getDimension(1));
    }

    @Test
    public void test_Retrieving_DimensionPixelSize() {
        Resources resources = Mockito.spy(this.resources);
        // We stub the transformation of R.string.test (number) -> R.string.test (string)
        Mockito.doReturn("R.string.test").when(resources).getResourceEntryName(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).getDimensionPixelSize(Mockito.anyInt());
        Mockito.doCallRealMethod().when(resources).put(Mockito.any(Item.class));

        resources.put(new Item<>("R.string.test", "22sp"));

        Assert.assertEquals(22, resources.getDimensionPixelSize(1));
    }

}
