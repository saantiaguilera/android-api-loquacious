package saantiaguilera.com.demoapp

import android.app.Application
import android.content.Context
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.locales.persistence.LocaleStore

/**
 * Created by saguilera on 11/19/17.
 */
class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        Loquacious.initialize(base!!)
                .with(LocaleStore(base))

        super.attachBaseContext(Loquacious.wrap(base))
    }

}