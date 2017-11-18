package com.saantiaguilera.loquacious;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saantiaguilera.loquacious.model.Item;
import com.saantiaguilera.loquacious.observer.LocaleBroadcastReceiver;
import com.saantiaguilera.loquacious.observer.OnLocaleChanged;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.util.LocaleUtil;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class LoquaciousTest {

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(Loquacious.class, "instance", null);
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", null);
    }

    @Test
    public void test_CantUseWithoutInit() {
        try {
            Loquacious.getInstance();
            Assert.fail();
        } catch (Exception ignored) {}
        try {
            Loquacious.getResources();
            Assert.fail();
        } catch (Exception ignored) {}
    }

    @Test
    public void test_Initialization_CreatesInstance() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        Assert.assertNotNull(Loquacious.getInstance());
    }

    @Test
    public void test_Resources_Exist_IfInitialized() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        Assert.assertNotNull(Loquacious.getResources());
    }

    @Test
    public void test_Adding_Subscriptor() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        OnLocaleChanged onLocaleChanged = Mockito.spy(OnLocaleChanged.class);
        Assert.assertTrue(Loquacious.getInstance().subscribe(onLocaleChanged));
    }

    @Test
    public void test_Adding_Subscriptor_WontHappenTwice() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        OnLocaleChanged onLocaleChanged = Mockito.spy(OnLocaleChanged.class);
        Assert.assertTrue(Loquacious.getInstance().subscribe(onLocaleChanged));
        Assert.assertFalse(Loquacious.getInstance().subscribe(onLocaleChanged));
    }

    @Test
    public void test_Removing_Subscriptor() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        OnLocaleChanged onLocaleChanged = Mockito.spy(OnLocaleChanged.class);
        Assert.assertTrue(Loquacious.getInstance().subscribe(onLocaleChanged));
        Assert.assertTrue(Loquacious.getInstance().unsubscribe(onLocaleChanged));
    }

    @Test
    public void test_Removing_NonExistentSubscriptor_IsTrue() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        OnLocaleChanged onLocaleChanged = Mockito.spy(OnLocaleChanged.class);
        Assert.assertTrue(Loquacious.getInstance().unsubscribe(onLocaleChanged));
    }

    @Test
    public void test_LocaleChanged_NotifiesSubscriptors() {
        Loquacious.initialize(RuntimeEnvironment.application, new Serializer() {
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
        });

        OnLocaleChanged onLocaleChanged = Mockito.spy(OnLocaleChanged.class);
        Assert.assertTrue(Loquacious.getInstance().subscribe(onLocaleChanged));

        new LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, null);

        Mockito.verify(onLocaleChanged).onLocaleChanged(Mockito.any(Locale.class));
    }

}
