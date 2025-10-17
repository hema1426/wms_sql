package com.winapp.KHDelivery.model.ItemBinLocation


import com.google.gson.annotations.SerializedName

data class ItemBinLocResponse(
    @SerializedName("responseData")
    var responseData: List<ResponseDataBinLocItem>,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("statusMessage")
    var statusMessage: String
)