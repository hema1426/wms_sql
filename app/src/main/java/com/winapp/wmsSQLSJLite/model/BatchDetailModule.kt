package com.winapp.wmsSQLSJLite.model

import com.google.gson.annotations.SerializedName

data class BatchDetailModule(
    @SerializedName("BatchNo")
    var batchNo: String?,
    @SerializedName("BatchQty")
    var batchQty: String?,
    @SerializedName("ItemCode")
    var itemCode: String?,
    var updateTime: String = "",
    var avlQty: String? = "0",
    ){
    var isRemove : Boolean = false
}