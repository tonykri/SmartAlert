package com.example.smartalert.responseData

import com.google.gson.annotations.SerializedName

class StatsResponseData (
    @SerializedName("total") val total: Int,
    @SerializedName("approved") val approved: Int
)