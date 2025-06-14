package com.winapp.pickanddrop.ui.model


import com.google.gson.annotations.SerializedName

data class PickIistDeliveryListingModel(
    @SerializedName("code")
    var code: String,
    @SerializedName("customerCode")
    var customerCode: String,
    @SerializedName("customerName")
    var customerName: String,
    @SerializedName("docDate")
    var docDate: String,
    @SerializedName("docNumber")
    var docNumber: String,
    @SerializedName("noOfItem")
    var noOfItem: String,
    @SerializedName("pickListStatus")
    var pickListStatus: String,
    @SerializedName("DateTime")
    var dateTime: String,
    var customerAddress: String,

    var isItemSelected: Boolean = false

)
