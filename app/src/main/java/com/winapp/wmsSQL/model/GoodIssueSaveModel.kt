package com.winapp.wmsSQL.model

data class GoodIssueSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodIssueSaveDetail>
)