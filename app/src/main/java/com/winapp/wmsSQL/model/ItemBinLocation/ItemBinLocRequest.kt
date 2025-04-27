package com.winapp.wmsSQL.model.ItemBinLocation


import com.google.gson.annotations.SerializedName

data class ItemBinLocRequest(
    @SerializedName("ItemCode")
    var itemCode: String,
    @SerializedName("WarehouseCode")
    var warehouseCode: String
)