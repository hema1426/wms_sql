package com.winapp.wmsSQLSJLite.model

data class GoodReceiptSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodReceiptSaveDetail>
)