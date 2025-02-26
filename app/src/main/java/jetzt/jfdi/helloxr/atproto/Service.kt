package jetzt.jfdi.skyxr.atproto

import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BskyService {
    @GET("xrpc/app.bsky.feed.searchPosts")
    suspend fun searchPosts(
        @Query("q") query: String,
        @Query("lang") language: String = "en",
        @Query("limit") limit: Int = 3,
    ): SearchResponse
}

fun createBskyService(): BskyService = Retrofit.Builder()
    .baseUrl("https://api.bsky.app/")
    .client(
        OkHttpClient.Builder().addInterceptor(
            LoggingInterceptor()
        ).build()
    )
    .addConverterFactory(
        Json {
            isLenient = true
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }.asConverterFactory(
            "application/json".toMediaType()
        )
    )
    .build()
    .create<BskyService>(
        BskyService::class.java
    )

private class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)

            val body = response.peekBody(Long.MAX_VALUE).string()

            val humanBody = try {
                val json = JSONObject(body)

                "\n->${json.toString(2)}"
            } catch (e: JSONException) {
                Log.e("JSON", "Parser error.", e)
                if (body.isNotBlank()) {
                    "\n${body.replace("\n", " ")}"
                } else {
                    ""
                }
            }

            val message =
                "${request.method} ${response.code}: ${request.url}$humanBody"
            Log.i("HTTP", message)

            return response
        } catch (throwable: Throwable) {
            Log.e("HTTP", "Error on HTTP", throwable)

            return Response.Builder().code(400).build()
        }
    }
}
