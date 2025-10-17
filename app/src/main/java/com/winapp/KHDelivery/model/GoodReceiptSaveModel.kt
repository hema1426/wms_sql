package com.winapp.KHDelivery.model

data class GoodReceiptSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodReceiptSaveDetail>
)