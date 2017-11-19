package saantiaguilera.com.demoapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by saguilera on 11/19/17.
 */
interface Repository {

    data class TextDto(val text: String)

    @GET("hello/{locale}")
    fun helloWorld(@Path("locale") locale: String): Call<TextDto>

}