package com.winapp.wmsSQLSJLite.model

data class GoodIssueSaveModel(
    val DocDate: String,
    val Remark: String,
    val GoodReceiveDetails: List<GoodIssueSaveDetail>
)