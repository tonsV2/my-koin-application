package dk.fitfit.mykoinapplication.rest

import android.content.Context
import dk.fitfit.mykoinapplication.ui.MainActivity.Companion.TOKEN_STORE

class AccessTokenProvider(private val context: Context) {
    fun token(): String? {
        val settings = context.getSharedPreferences(TOKEN_STORE, Context.MODE_PRIVATE)
        return settings.getString("accessToken", null)
    }

    fun refreshToken(): String? = null
}
