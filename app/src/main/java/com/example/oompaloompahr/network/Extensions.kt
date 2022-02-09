package com.example.oompaloompahr.network

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Log.d("Network", "throw $throwable")
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    Log.d("Network","EL CODIGO ES $code")
                    ResultWrapper.HttpError(code)
                }
                else -> {
                    Log.d("Network","Error en el código")
                    throwable.printStackTrace()
                    ResultWrapper.HttpError(-1)
                }
            }
        }
    }
}

enum class HttpHandler(val code: Int){
    NetworkError(-1),
    AlreadyHandled(-2)
}
