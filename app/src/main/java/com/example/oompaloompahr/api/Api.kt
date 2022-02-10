package com.example.oompaloompahr.api

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_API_URL = "https://2q2woep105.execute-api.eu-west-1.amazonaws.com/napptilus/"

/**
 * Clase donde se gestionan las peticiones a la API.
 * La constante BASE_API_URL es la url básica de la aplicación, donde el resto de peticiones
 * se construirán en base a esta.
 *
 * Se ha utilizado Retrofit con Gson para hacer las peticiones y construir las respuestas.
 */
class Api {

    var gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    /**
     * Interfaz donde se han construido las peticiones.
     */
    interface MyApiEndpointInterface {

        @GET("oompa-loompas")
        fun getOmpas(@Query("page") pageNumber: Int): Call<Data>

        @GET("oompa-loompas/{id}")
        fun getOmpa(@Path("id") id: Int): Call<OompaLoompa>
    }

    var apiService = retrofit.create(MyApiEndpointInterface::class.java)

    fun getOmpas(page: Int, res: (data: Data) -> Unit) {
        val call: Call<Data> = apiService.getOmpas(page)
        call.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                val data = response.body()
                if (data != null) {
                    res(data)
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable?) {
                // Log error here since request failed
                t?.printStackTrace()
            }
        })
    }

    fun getOmpa(ompaId: Int, res: (oompa: OompaLoompa) -> Unit) {
        val call: Call<OompaLoompa> = apiService.getOmpa(ompaId)
        call.enqueue(object : Callback<OompaLoompa> {
            override fun onResponse(call: Call<OompaLoompa>, response: Response<OompaLoompa>) {
                val oompa = response.body()
                if (oompa != null) {
                    res(oompa)
                }
            }

            override fun onFailure(call: Call<OompaLoompa>, t: Throwable?) {
                // Log error here since request failed
                t?.printStackTrace()
            }
        })
    }
}
