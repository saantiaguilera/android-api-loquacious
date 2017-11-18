package com.saantiaguilera.loquacious.observer;

import android.support.annotation.NonNull;

/**
 * Created by saguilera on 11/18/17.
 */

public interface LocaleSubscribable {
    boolean subscribe(@NonNull OnLocaleChanged subscriptor);
    boolean unsubscribe(@NonNull OnLocaleChanged subscriptor);
}
