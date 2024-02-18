package com.example.smartalert.responseData

import com.google.gson.annotations.SerializedName

class UserRequestResponseData (
    @SerializedName("id") val id: String,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("message") val message: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("approved") val approved: Boolean,
    @SerializedName("similarRequests") val similarRequests: Int
)