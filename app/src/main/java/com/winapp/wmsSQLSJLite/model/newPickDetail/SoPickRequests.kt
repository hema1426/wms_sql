package com.winapp.wmsSQLSJLite.model.newPickDetail

data class SoPickRequests(
    val createUser: String,
    val CurrentDateTime: String,
    val CustomerCode: String,
    val SODate: String,
    val SONumber: String,
    val OrderStatus: String,
    val SOStatus: String,
    val SalesOrderDetails: ArrayList<SalesOrderPickDetail>
)