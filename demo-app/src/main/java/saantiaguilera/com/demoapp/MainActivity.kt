package saantiaguilera.com.demoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.parse.Serializer
import com.saantiaguilera.loquacious.util.LocaleUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var helloWorldText: TextView

    /**
     * This should be done in an Application class
     */
    private fun initializeLoquacious() {
        Loquacious.initialize(this, object : Serializer {
            override fun <T> serialize(item: Item<T>): String {
                return Gson().toJson(item)
            }

            override fun <T> hidrate(string: String, classType: Class<T>): Item<T> {
                return Gson().fromJson(string, object : TypeToken<Item<T>>() {}.type)
            }
        })
    }

    /**
     * We subscribe to locale changes, so we ask again the server for its localized strings
     */
    private fun subscribe() = Loquacious.getInstance().subscribe { locale -> request(locale.displayLanguage) }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeLoquacious()
        subscribe()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helloWorldText = findViewById(R.id.helloWorld)

        // Request the locale for the starting settings. We should ofc do this only once to avoid
        // useless api calls
        request(LocaleUtil.current()?.displayLanguage!!)
    }

    fun updateHelloWorld() {
        helloWorldText.text = Loquacious.getResources().getString(R.string.hello_world)
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
                        Loquacious.getResources().put(Item("R.string.hello_world", response?.body()?.text))
                        updateHelloWorld()
                    }
                })
    }

}
