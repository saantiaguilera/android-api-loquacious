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
                    "en", "en_US" -> "{ \"text\": \"Hello World!\" }"
                    "es" -> "{ \"text\": \"Hola Mundo!\" }"
                    "sv" -> "{ \"text\": \"Hej världen!\" }"
                    else -> {
                        "{ \"text\": \"Locale not supported :(\" }"
                    }
                }
                string
            }
        }
    }
}
