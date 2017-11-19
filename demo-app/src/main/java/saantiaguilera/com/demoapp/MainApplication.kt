package saantiaguilera.com.demoapp

import android.app.Application
import android.content.Context
import com.saantiaguilera.loquacious.Loquacious

/**
 * Created by saguilera on 11/19/17.
 */
class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        initializeLoquacious(base!!)
        super.attachBaseContext(Loquacious.wrap(base))
    }

    private fun initializeLoquacious(context: Context) {
        Loquacious.initialize(context)
    }

}