package dk.fitfit.mykoinapplication.rest.service

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/login")
    suspend fun login(@Body credentials: Credentials): OauthTokens
}

class Credentials(val username: String, val password: String)

class OauthTokens(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("username") val username: String
)
