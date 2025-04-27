package com.winapp.wmsSQL.model.newPickDetail

data class SoRequests(
    val createUser: String,
    val CurrentDateTime: String,
    val CustomerCode: String,
    val SODate: String,
    val SONumber: String,
    val OrderStatus: String,
    val SOStatus: String,
    val SalesOrderDetails: List<SalesOrderDetail>
)