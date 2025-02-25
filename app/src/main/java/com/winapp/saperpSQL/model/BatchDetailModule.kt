package com.winapp.saperpSQL.model

import com.google.gson.annotations.SerializedName

data class BatchDetailModule(
    @SerializedName("BatchNo")
    var batchNo: String?,
    @SerializedName("BatchQty")
    var batchQty: String?,
    @SerializedName("ItemCode")
    var itemCode: String?,
    var updateTime: String = "",
){
    var isRemove : Boolean = false
}