package org.the_chance.honeymart.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Response
import org.the_chance.honeymart.data.source.local.AuthDataStorePref
import javax.inject.Inject

/**
 * Created by Aziza Helmy on 6/16/2023.
 */
class AuthInterceptor @Inject constructor(
    private val dataStorePref: AuthDataStorePref
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val testToken =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXVkIjoiaW8ua3Rvci5zZXJ2ZXIuY29uZmlnLk1hcEFwcGxpY2F0aW9uQ29uZmlnQGMxZmNhMmEiLCJST0xFX1RZUEUiOiJOb3JtYWxVc2VyIiwiaXNzIjoiaW8ua3Rvci5zZXJ2ZXIuY29uZmlnLk1hcEFwcGxpY2F0aW9uQ29uZmlnQDJjNzY4YWRhIiwiZXhwIjoxNjg3NDY2NTk1fQ.e4DIqZSfF6ppvQDiz7uzBjKuHaJNIo1xbblNSvo6xDg"
        val token = dataStorePref.getToken()
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader(AUTHORIZATION, "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}