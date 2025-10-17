package com.winapp.KHDelivery.model.newPickDetail


import com.google.gson.annotations.SerializedName

data class NewpickDetailRespData(
    @SerializedName("address1")
    var address1: String,
    @SerializedName("address2")
    var address2: String,
    @SerializedName("address3")
    var address3: String,
    @SerializedName("attachmentEntry")
    var attachmentEntry: String,
    @SerializedName("balanceAmount")
    var balanceAmount: Double,
    @SerializedName("sumofCartonQty")
    var sumofCartonQty: Double,
    @SerializedName("sumofPcsQty")
    var sumofPcsQty: Double,
    @SerializedName("billDiscount")
    var billDiscount: String,
    @SerializedName("block")
    var block: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("code")
    var code: String,
    @SerializedName("companyCode")
    var companyCode: String,
    @SerializedName("contactPersonCode")
    var contactPersonCode: String,
    @SerializedName("countryName")
    var countryName: String,
    @SerializedName("createDate")
    var createDate: String,
    @SerializedName("currencyCode")
    var currencyCode: String,
    @SerializedName("currencyName")
    var currencyName: String,
    @SerializedName("customerCode")
    var customerCode: String,
    @SerializedName("customerName")
    var customerName: String,
    @SerializedName("discountPercentage")
    var discountPercentage: String,
    @SerializedName("docEntry")
    var docEntry: String,
    @SerializedName("fDocTotal")
    var fDocTotal: String,
    @SerializedName("fTaxAmount")
    var fTaxAmount: Double,
    @SerializedName("fTotal")
    var fTotal: Double,
    @SerializedName("gstNo")
    var gstNo: String,
    @SerializedName("iPaidAmount")
    var iPaidAmount: Double,
    @SerializedName("iTotalDiscount")
    var iTotalDiscount: Double,
    @SerializedName("netTotal")
    var netTotal: String,
    @SerializedName("noOfOutstandingInvoice")
    var noOfOutstandingInvoice: String,
    @SerializedName("paidAmount")
    var paidAmount: Double,
    @SerializedName("phoneNo")
    var phoneNo: String,
    @SerializedName("receivedAmount")
    var receivedAmount: Double,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("salesOrderDetails")
    var salesOrderDetails: ArrayList<NewSalesOrderDetailItem>,
    @SerializedName("signFlag")
    var signFlag: String,
    @SerializedName("signature")
    var signature: Any,
    @SerializedName("soDate")
    var soDate: String,
    @SerializedName("formattedTime")
    var time: String,
    @SerializedName("soNumber")
    var soNumber: String,
    @SerializedName("soStatus")
    var soStatus: String,
    @SerializedName("state")
    var state: String,
    @SerializedName("street")
    var street: String,
    @SerializedName("subTotal")
    var subTotal: Double,
    @SerializedName("taxCode")
    var taxCode: String,
    @SerializedName("taxPerc")
    var taxPerc: String,
    @SerializedName("taxPercentage")
    var taxPercentage: String,
    @SerializedName("taxTotal")
    var taxTotal: Double,
    @SerializedName("taxType")
    var taxType: String,
    @SerializedName("total")
    var total: Double,
    @SerializedName("totalDiscount")
    var totalDiscount: String,
    @SerializedName("updateDate")
    var updateDate: String,
    @SerializedName("zipcode")
    var zipcode: String
)