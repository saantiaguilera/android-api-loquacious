package com.saantiaguilera.loquacious.resource;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ibm.icu.text.PluralRules;
import com.saantiaguilera.loquacious.model.Item;
import com.saantiaguilera.loquacious.model.Quantity;
import com.saantiaguilera.loquacious.parse.Serializer;
import com.saantiaguilera.loquacious.persistence.LoquaciousStore;
import com.saantiaguilera.loquacious.persistence.Store;
import com.saantiaguilera.loquacious.util.LocaleUtil;
import com.saantiaguilera.loquacious.util.Mangler;

import java.util.List;

/**
 * Created by saguilera on 11/18/17.
 */
public class Resources extends android.content.res.Resources
        implements Store.Commit, Store.Clear {

    private @NonNull LoquaciousStore store;

    public Resources(@NonNull Context context, @NonNull Serializer serializer) {
        super(context.getAssets(), context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        store = new LoquaciousStore(context, serializer);
    }

    @Nullable
    @CheckResult
    private <ReturnType> ReturnType fetch(@NonNull String entryName, @NonNull Class<ReturnType> returnTypeClass) {
        Item<ReturnType> storeResponse = store.fetch(entryName, returnTypeClass);
        if (storeResponse != null) {
            return storeResponse.getValue();
        }
        return null;
    }

    @Nullable
    @CheckResult
    private <ReturnType> ReturnType get(int id, @NonNull Class<ReturnType> returnTypeClass) {
        return fetch(Mangler.mangle(getResourceEntryName(id)), returnTypeClass);
    }

    @Nullable
    @CheckResult
    private <ReturnType> ReturnType get(int id, int quantity, @NonNull Class<ReturnType> returnTypeClass) {
        return fetch(
                Mangler.mangle(
                        getResourceEntryName(id),
                        Quantity.from(PluralRules.forLocale(LocaleUtil.current()), quantity)
                ),
                returnTypeClass
        );
    }

    //------------------------------------- Resources getter -------------------------------------//

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        String response = get(id, String.class);

        if (response != null) {
            return response;
        }
        return super.getString(id);
    }

    @NonNull
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return String.format(LocaleUtil.current(), getString(id), formatArgs);
    }

    @NonNull
    @Override
    public String[] getStringArray(int id) throws NotFoundException {
        List<String> response = get(id, List.class);

        if (response != null) {
            return response.toArray(new String[response.size()]);
        }
        return super.getStringArray(id);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity) throws NotFoundException {
        String response = get(id, quantity, String.class);

        if (response != null) {
            return response;
        }
        return super.getQuantityString(id, quantity);
    }

    @NonNull
    @Override
    public String getQuantityString(int id, int quantity, Object... formatArgs) throws NotFoundException {
        return String.format(LocaleUtil.current(), getQuantityString(id, quantity), formatArgs);
    }

    @Override
    public boolean getBoolean(int id) throws NotFoundException {
        Boolean response = get(id, Boolean.class);

        if (response != null) {
            return response;
        }
        return super.getBoolean(id);
    }

    @NonNull
    @Override
    public CharSequence getText(int id) throws NotFoundException {
        CharSequence response = get(id, CharSequence.class);

        if (response != null) {
            return response;
        }
        return super.getText(id);
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        CharSequence response = get(id, CharSequence.class);

        if (response != null) {
            return response;
        }
        return super.getText(id, def);
    }

    @NonNull
    @Override
    public CharSequence[] getTextArray(int id) throws NotFoundException {
        List<String> response = get(id, List.class);

        if (response != null) {
            return response.toArray(new String[response.size()]);
        }
        return super.getTextArray(id);
    }

    @NonNull
    @Override
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        CharSequence response = get(id, quantity, CharSequence.class);

        if (response != null) {
            return response;
        }
        return super.getQuantityText(id, quantity);
    }

    @Override
    public int getInteger(int id) throws NotFoundException {
        Double response = get(id, Double.class);

        if (response != null) {
            return response.intValue();
        }
        return super.getInteger(id);
    }

    @NonNull
    @Override
    public int[] getIntArray(int id) throws NotFoundException {
        List<Double> response = get(id, List.class);

        if (response != null) {
            int[] arr = new int[response.size()];
            for (int i = 0 ; i < response.size() ; i++) { arr[i] = response.get(i).intValue(); }
            return arr;
        }
        return super.getIntArray(id);
    }

    @Override
    public float getDimension(int id) throws NotFoundException {
        String response = get(id, String.class);

        if (response != null) {
            return DimensionConverter.stringToDimension(response, getDisplayMetrics());
        }
        return super.getDimension(id);
    }

    @Override
    public int getDimensionPixelSize(int id) throws NotFoundException {
        String response = get(id, String.class);

        if (response != null) {
            return DimensionConverter.stringToDimensionPixelSize(response, getDisplayMetrics());
        }
        return super.getDimensionPixelSize(id);
    }

    //------------------------------------- Store interface --------------------------------------//

    /**
     * Delegate
     */
    @Override
    public <Type> void put(@NonNull Item<Type> item) {
        store.put(item);
    }

    /**
     * Delegate
     */
    @Override
    public <Type> void putAll(@NonNull List<Item<Type>> items) {
        store.putAll(items);
    }

    /**
     * Delegate
     */
    @Override
    public void clear() {
        store.clear();
    }

}
