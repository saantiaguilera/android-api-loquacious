package saantiaguilera.com.demoapp

import android.app.Application
import android.content.Context
import com.saantiaguilera.loquacious.Loquacious

/**
 * Created by saguilera on 11/19/17.
 */
class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) = super.attachBaseContext(Loquacious.wrap(base!!))

}