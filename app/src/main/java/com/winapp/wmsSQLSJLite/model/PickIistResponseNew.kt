package com.winapp.pickanddrop.ui.model


import com.google.gson.annotations.SerializedName

data class PickIistResponseNew(
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
    @SerializedName("docStatus")
    var docStatus: String,
    @SerializedName("noOfItem")
    var noOfItem: String,
    @SerializedName("pickListStatus")
    var pickListStatus: String,
    @SerializedName("PickListNumber")
    var pickListNo: String,
    @SerializedName("salesEmployee")
    var salesEmployee: String,
    @SerializedName("OwnerName")
    var ownerName: String,
    @SerializedName("DateTime")
    var dateTime: String,
    @SerializedName("pickedQty")
    var pickedCount: String,
    @SerializedName("nonPickedQty")
    var nonPickedCount: String,

    var isItemSelected: Boolean = false

)
