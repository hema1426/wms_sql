package com.winapp.saperp.model

data class GoodReceiptSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodReceiptSaveDetail>
)