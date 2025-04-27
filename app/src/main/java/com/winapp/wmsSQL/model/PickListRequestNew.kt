package com.winapp.pickanddrop.ui.model

import com.google.gson.annotations.SerializedName

data class PickListRequestNew(
    @SerializedName("CustomerCode")
    var customerCode: String,
    @SerializedName("DocNumber")
    var docNo: String,
    @SerializedName("DocStatus")
    var docStatus: String,
    @SerializedName("CustomerName")
    var docType: String,
    @SerializedName("FromDate")
    var fromDate: String,
    @SerializedName("ToDate")
    var toDate: String,
    @SerializedName("UserCode")
    var user: String,
)
//{"CustomerCode":"C003-GE-GT-001","CustomerName":"Abdul Munaff Enterprise","UserCode":"DELIXP01","FromDate":"20230106","ToDate":"20231123","DocNumber":"24010002","DocStatus":"Y"}