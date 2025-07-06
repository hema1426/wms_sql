package com.winapp.wmsSQL.model


import com.google.gson.annotations.SerializedName

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
    var invNumber: String,
    @SerializedName("noOfItem")
    var noOfItem: String,
    @SerializedName("pickListStatus")
    var invoiceStatus: String,
    @SerializedName("DateTime")
    var dateTime: String,
    var pickListStatus: String,
    var customerAddress: String,
    var shipAddress: String,
    var phoneNo: String,
    var contactName: String,
    var remark: String,
    var user: String,
    var signatureUrl: String,
    var imageUrl: String,
    var mobileUser: String,
    var isShow: Boolean = false,
    var isItemSelected: Boolean = false,
    var invoiceList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>? = null
){
}
