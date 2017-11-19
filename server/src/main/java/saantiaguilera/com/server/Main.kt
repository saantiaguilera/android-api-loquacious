package saantiaguilera.com.server

import spark.kotlin.get

/**
 * Created by saguilera on 11/19/17.
 */
class Main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            get("/hello/:locale") {
                status(200)
                val string = when (params(":locale")) {
                    "English", "english" -> "{ \"text\": \"Hello World!\" }"
                    "Spanish", "spanish", "Español", "español" -> "{ \"text\": \"Hola Mundo!\" }"
                    "Swedish", "Sverige", "Svenska", "svenska", "sverige", "swedish" -> "{ \"text\": \"Hej världen!\" }"
                    else -> {
                        "{ \"text\": \"Locale not supported :(\" }"
                    }
                }
                string
            }
        }
    }
}
