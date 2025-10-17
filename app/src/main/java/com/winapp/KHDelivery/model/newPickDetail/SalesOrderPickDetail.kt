package com.winapp.KHDelivery.model.newPickDetail

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