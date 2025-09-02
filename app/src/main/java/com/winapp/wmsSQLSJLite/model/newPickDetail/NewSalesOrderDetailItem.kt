package com.winapp.wmsSQLSJLite.model.newPickDetail


import com.google.gson.annotations.SerializedName

data class NewSalesOrderDetailItem(
    @SerializedName("price")
    var price: Double,
    @SerializedName("productCode")
    var productCode: String,
    @SerializedName("productName")
    var productName: String,
    @SerializedName("uoMCode")
    var uomCode: Any,
    @SerializedName("uoMName")
    var uoMName: Any,
    @SerializedName("stockInHand")
    var stockInHand: Double,
    @SerializedName("pickedQty")
    var pickedQuantity: Double,
    @SerializedName("releasedQty")
    var releasedQty: Double,
    @SerializedName("balance")
    var balance: Int,
    @SerializedName("lineNum")
    var lineNum: Int?,
    @SerializedName("remarks")
    var remarks: String,
    @SerializedName("palletRemarks")
    var palletRemarks: String,
    @SerializedName("total")
    var total: Double,
    @SerializedName("cartonPrice")
    var cartonPrice: Double,
    @SerializedName("cartonQty")
    var cartonQty: Double,
    @SerializedName("pcsQty")
    var pcsQty: Double,
    @SerializedName("noofPallet")
    var palletQty: String,
    @SerializedName("quantity")
    var quantity: Double,
    @SerializedName("frozen")
    var frozen: String,
    @SerializedName("foreignName")
    var foreignName: String,
    @SerializedName("manageBatchOrSerial")
    var isManageBatch: String,
    @SerializedName("availableBatchDetails")
    var batchDetails: ArrayList<BatchDetailPickModule> = arrayListOf(),
    @SerializedName("batchDetails")
    var selectedBatchDetails: ArrayList<BatchDetailPickModule> = arrayListOf(),
    @SerializedName("imageURL")
    var imageUrl: String,
    @SerializedName("imageString")
    var imageString: String,
    @SerializedName("additionalRemarks")
    var joinBatchQty: String?,
    var orderStatus: String,
    var kgQtyStatus: Double,

    var pickedqQtyOld: Int,
    var balQtyOld: Int,
    var isCheckboxVisible: Boolean = false,
    var iseditpick: Boolean = false,
    var isCheckboxSelect: Boolean = false,
    var isDynamic: Boolean = false,
    var kgQty: Double = 0.00,
    var isKgQty: Boolean = false,
){
    var action: String = ""
}