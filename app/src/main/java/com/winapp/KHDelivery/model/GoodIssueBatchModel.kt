package com.winapp.KHDelivery.model


data class GoodIssueBatchModel(
    var batchNum: String,
    var avlQty: String,
    var sysNumber: String,
    var batchQty: String?,
    var itemCode: String?,
    var updateTime: String = "",
){
    var isRemove : Boolean = false
}