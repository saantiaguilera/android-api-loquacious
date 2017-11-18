package com.saantiaguilera.loquacious.observer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saantiaguilera.loquacious.Loquacious;
import com.saantiaguilera.loquacious.model.Item;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.util.LocaleUtil;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by saguilera on 11/18/17.
 */
@RunWith(RobolectricTestRunner.class)
public class LocaleBroadcastReceiverTest {

    @After
    public void tearDown() {
        ReflectionHelpers.setStaticField(Loquacious.class, "instance", null);
        ReflectionHelpers.setStaticField(LocaleUtil.class, "current", null);
    }

    @Test
    public void test_LocaleChanges_DispatchesToLoquacious() {
        Loquacious loquacious = Mockito.mock(Loquacious.class);
        ReflectionHelpers.setField(loquacious, "onLocaleChangedList", new ArrayList<>());
        loquacious = Mockito.spy(loquacious);
        ReflectionHelpers.setStaticField(Loquacious.class, "instance", loquacious);
        new LocaleBroadcastReceiver().onReceive(RuntimeEnvironment.application, null);

        Mockito.verify(loquacious).onLocaleChanged(Mockito.any(Locale.class));
    }

    @Test
    public void test_LocaleChanges_SetsNewLocale() {
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

        Context context = Mockito.spy(RuntimeEnvironment.application);

        new LocaleBroadcastReceiver().onReceive(context, null);

        // To avoid including power mock, this is my best change of verifying that we are trying to obtain
        // the current locale
        Mockito.verify(context).getResources();
    }

}
