package saantiaguilera.com.demoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saantiaguilera.loquacious.Loquacious
import com.saantiaguilera.loquacious.model.Item
import com.saantiaguilera.loquacious.parse.Serializer

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

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeLoquacious()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        helloWorldText = findViewById(R.id.helloWorld)

        Loquacious.getResources().put(Item("R.string.hello_world", "Hello World"))
    }

    override fun onResume() {
        super.onResume()
        helloWorldText.text = Loquacious.getResources().getString(R.string.hello_world)
    }

}
