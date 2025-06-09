package com.winapp.wmsSQL.model.newPickDetail

import com.google.gson.annotations.SerializedName

data class SalesOrderPickDetail(
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
    var lineNum: String?,
    )