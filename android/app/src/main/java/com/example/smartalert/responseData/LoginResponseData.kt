package com.example.smartalert.responseData

import com.google.gson.annotations.SerializedName

data class LoginResponseData(
    @SerializedName("firstname") val firstname: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("accessToken") val accessToken: String
)