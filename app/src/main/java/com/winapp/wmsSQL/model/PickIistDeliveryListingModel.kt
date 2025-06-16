package com.winapp.wmsSQL.model


import com.google.gson.annotations.SerializedName
import com.winapp.wmsSQL.model.InvoicePrintPreviewModel

data class PickIistDeliveryListingModel(
    @SerializedName("code")
    var code: String,
    @SerializedName("customerCode")
    var customerCode: String,
    @SerializedName("customerName")
    var customerName: String,
    @SerializedName("docDate")
    var docDate: String,
    @SerializedName("docNumber")
    var docNumber: String,
    @SerializedName("noOfItem")
    var noOfItem: String,
    @SerializedName("pickListStatus")
    var pickListStatus: String,
    @SerializedName("DateTime")
    var dateTime: String,
    var customerAddress: String,
     var isShow: Boolean = false,
    var isItemSelected: Boolean = false,
    var invoiceList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>? = null
){
}
