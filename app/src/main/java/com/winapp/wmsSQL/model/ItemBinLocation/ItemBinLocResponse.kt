package com.winapp.wmsSQL.model.ItemBinLocation


import com.google.gson.annotations.SerializedName
import com.winapp.wmsSQL.model.ItemBinLocation.ResponseDataBinLocItem

data class ItemBinLocResponse(
    @SerializedName("responseData")
    var responseData: List<ResponseDataBinLocItem>,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("statusMessage")
    var statusMessage: String
)