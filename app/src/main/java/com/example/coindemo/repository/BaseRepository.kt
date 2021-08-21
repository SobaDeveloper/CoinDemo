package com.example.coindemo.repository

import com.example.coindemo.data.Result
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> getResponse(
        request: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = request.invoke()
            return if (response.succeeded()) Result.Success(response.body()!!)
            else Result.Error(response.message())
        } catch (e: Throwable) {
            Result.Error(e.toString())
        }
    }

    private fun <T> Response<T>?.succeeded(): Boolean =
        this != null && this.isSuccessful && this.body() != null
}