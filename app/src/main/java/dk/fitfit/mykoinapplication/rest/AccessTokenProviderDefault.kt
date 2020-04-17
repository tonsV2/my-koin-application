package dk.fitfit.mykoinapplication.rest

import android.content.Context
import dk.fitfit.mykoinapplication.ui.MainActivity.Companion.TOKEN_STORE

class AccessTokenProviderDefault(private val context: Context) : AccessTokenProvider {
    override fun token(): String? {
        val settings = context.getSharedPreferences(TOKEN_STORE, Context.MODE_PRIVATE)
        return settings.getString("accessToken", null)
    }

    override fun refreshToken(): String? = null
}
