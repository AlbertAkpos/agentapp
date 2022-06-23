package com.youverify.agent_app_android.data.api

import com.youverify.agent_app_android.util.AgentSharePreference
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val sharePreference: AgentSharePreference) : Interceptor {

    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (token.isNullOrEmpty()) token = sharePreference.token
        var request = chain.request()
        if (request.header("No-Authentication") == null) {
            if (!token.isNullOrEmpty()) {
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            }
        }
        return chain.proceed(request)
    }
}