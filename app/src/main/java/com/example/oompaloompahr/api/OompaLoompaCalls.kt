package com.example.oompaloompahr.api

import android.util.Log
import com.example.oompaloompahr.network.HttpHandler
import com.example.oompaloompahr.network.ResultWrapper
import com.example.oompaloompahr.network.safeApiCall
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

private const val BASE_API_URL = "https://2q2woep105.execute-api.eu-west-1.amazonaws.com/napptilus/oompa-loompas/"

private val moshi = Moshi.Builder()
    .build()

private val willywonka = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    .baseUrl(BASE_API_URL)
    .build()

private fun <T> createApiclient(
    url: String,
    apiDescription: Class<T>,
    gsonBuilder: GsonBuilder,
): T {
    val builder = OkHttpClient.Builder()

    builder.connectTimeout((180 * 1000).toLong(), TimeUnit.MILLISECONDS)
    builder.readTimeout((180 * 1000).toLong(), TimeUnit.MILLISECONDS)
    builder.writeTimeout((180 * 1000).toLong(), TimeUnit.MILLISECONDS)
    val gson = gsonBuilder.create()
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(builder.build())
        .build()
    return retrofit.create(apiDescription)
}


/**
 * A public interface that exposes the methods
 */
interface ApiService {

    @GET("?page=1")
    fun getOompaLoompas(): Call<Data>

    @GET("{oompa_id}")
    suspend fun findOompaLoompa(@Path("oompa_id") oompa_id: Int): OompaLoompa
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ApiCall {
    val retrofitService: ApiService by lazy { willywonka.create(ApiService::class.java) }
}

suspend fun getOompaLoompas(cb: ArgumentCallback<Data>) {
    val response = safeApiCall(Dispatchers.IO) { ApiCall.retrofitService.getOompaLoompas() }
    Log.d("MainViewModel" ,"GET OOMPA LOOMPAS $response")
    when (response) {
        is ResultWrapper.NetworkError -> Log.d("JPS ERROR",
            HttpHandler.NetworkError.code.toString()
        )
        is ResultWrapper.HttpError -> Log.d("JPS HTTP ERROR", response.code.toString())
        is ResultWrapper.Success -> {
            response.value.enqueue(getOompaCallback(cb))
        }
    }
    Log.d("TEST", "response: $response")
}

    private fun getOompaCallback(cb: ArgumentCallback<Data>): Callback<Data> {
        return object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                val u: Data? = response.body()
               cb.done(u)
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                cb.error("JPS ERROR", t)
            }

        }
    }
