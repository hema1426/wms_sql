package com.winapp.KHDelivery.model.newPickDetail


import com.google.gson.annotations.SerializedName

data class BatchDetailPickModule(
    @SerializedName("batchNo")
    var batchNo: String?,
    @SerializedName("batchQty")
    var batchQty: Double?,
    @SerializedName("batchitemcode")
    var itemCode: String?,
    var selectKgQty: Double?,
    var isFrozen: String?,
    var batchPos: Int? = -1
    )
{
//    var isRemove : Boolean = false
    var totalKG : Double? =0.00

}
