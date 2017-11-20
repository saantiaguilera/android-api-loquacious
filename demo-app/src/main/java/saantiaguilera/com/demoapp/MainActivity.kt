package saantiaguilera.com.demoapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.saantiaguilera.locales.observer.subscribe
import com.saantiaguilera.locales.util.LocaleUtil
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var helloWorldText: TextView

    /**
     * We subscribe to locale changes, so we ask again the server for its localized strings
     */
    private fun subscribe() = Loquacious.instance.subscribe { locale -> request(locale.toString()) }

    override fun attachBaseContext(newBase: Context?) = super.attachBaseContext(Loquacious.wrap(newBase!!))

    override fun onCreate(savedInstanceState: Bundle?) {
        subscribe()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helloWorldText = findViewById(R.id.helloWorld)

        // Request the locale for the starting settings. We should ofc do this only once to avoid
        // useless api calls
        request(LocaleUtil.current()?.toString()!!)
    }

    fun updateHelloWorld() {
        helloWorldText.text = resources.getString(R.string.hello_world)
    }

    private fun request(locale: String) {
        Retrofit.Builder()
                .baseUrl("http://10.0.2.2:4567/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<Repository>(Repository::class.java)
                .helloWorld(locale).enqueue(object : Callback<Repository.TextDto> {
                    override fun onFailure(call: Call<Repository.TextDto>?, t: Throwable?) {
                        Toast.makeText(this@MainActivity, "Oops, error in the request", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<Repository.TextDto>?, response: Response<Repository.TextDto>?) {
                        Loquacious.resources.put(Item(R.string.hello_world, response?.body()?.text))
                        updateHelloWorld()
                    }
                })
    }

}
