package com.winapp.wmsSQLSJLite.model.ItemBinLocation


import com.google.gson.annotations.SerializedName

data class ItemBinLocRequest(
    @SerializedName("ItemCode")
    var itemCode: String,
    @SerializedName("WarehouseCode")
    var warehouseCode: String
)