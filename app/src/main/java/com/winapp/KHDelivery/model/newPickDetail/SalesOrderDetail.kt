package com.winapp.KHDelivery.model.newPickDetail

import com.google.gson.annotations.SerializedName

data class SalesOrderDetail(
    @SerializedName("AdditionalRemarks")
    var joinBatchQty: String?,
    val BatchDetails: List<BatchDetailPickModule>,
    val Batch: String,
    val NoofCarton: String,
    val NoofPCS: String,
    val NoofPallet: String,
    val ProductCode: String,
    val Quantity: Double,
    val Remarks: String,
    val PalletRemarks: String,
    val UnitPrice: Double,
    val UomCode: String,
    val WarehouseCode: String,
    var lineNum: Int?,

    )