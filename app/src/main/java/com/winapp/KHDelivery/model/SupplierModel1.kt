package com.winapp.KHDelivery.model


import com.google.gson.annotations.SerializedName

data class SupplierModel1(
    @SerializedName("customerCode")
    var customerCode: String,
    @SerializedName("customerName")
    var customerName: String)