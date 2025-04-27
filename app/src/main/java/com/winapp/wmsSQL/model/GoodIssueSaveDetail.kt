package com.winapp.wmsSQL.model

import com.google.gson.annotations.SerializedName

data class GoodIssueSaveDetail(
    @SerializedName("BatchDetails")
    val BatchDetails: List<BatchDetailModule>,
    val ItemCode: String,
    val Price: String,
    val UomCode: String,
    val WarehouseCode: String,
    val qty: String
)