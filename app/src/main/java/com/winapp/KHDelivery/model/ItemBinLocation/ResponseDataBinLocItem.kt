package com.winapp.KHDelivery.model.ItemBinLocation


import com.google.gson.annotations.SerializedName

data class ResponseDataBinLocItem(
    @SerializedName("binLocationCode")
    var binLocationCode: String,
    @SerializedName("itemCode")
    var itemCode: String,
    @SerializedName("onHandQty")
    var onHandQty: Int,
    @SerializedName("whsCode")
    var whsCode: String
)