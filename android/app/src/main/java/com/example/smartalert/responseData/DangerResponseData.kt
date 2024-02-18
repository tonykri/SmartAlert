package com.example.smartalert.responseData

import com.google.gson.annotations.SerializedName

class DangerResponseData (
    @SerializedName("id") val id: String,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("category") val category: Category,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("noOfRequests") val noOfRequests: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
){
    data class Category(
        @SerializedName("name")
        val name: String,
        @SerializedName("protectionEn")
        val protectionEn: String,
        @SerializedName("protectionEl")
        val protectionEl: String,
        @SerializedName("dangerRay")
        val dangerRay: Int
    )
}
