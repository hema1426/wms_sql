package com.winapp.KHDelivery.model

data class GoodIssueSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodIssueSaveDetail>
)