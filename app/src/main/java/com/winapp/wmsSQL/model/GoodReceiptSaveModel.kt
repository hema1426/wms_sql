package com.winapp.wmsSQL.model

data class GoodReceiptSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodReceiptSaveDetail>
)