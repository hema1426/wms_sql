package com.winapp.KHDelivery.model.newPickDetail


import com.google.gson.annotations.SerializedName

data class NewSalesOrderDetailItem1(
    @SerializedName("barCodes")
    var barCodes: String,
    @SerializedName("cartonPrice")
    var cartonPrice: Double,
    @SerializedName("cartonQty")
    var cartonQty: Double,
    @SerializedName("pcsQty")
    var pcsQty: Double,
    @SerializedName("noofPallet")
    var palletQty: String,
    @SerializedName("companyCode")
    var companyCode: String,
    @SerializedName("createDate")
    var createDate: String,
    @SerializedName("createdUser")
    var createdUser: String,
    @SerializedName("currency")
    var currency: String,
    @SerializedName("customerCategoryNo")
    var customerCategoryNo: String,
    @SerializedName("discountPercentage")
    var discountPercentage: Double,
    @SerializedName("dueDate")
    var dueDate: String,
    @SerializedName("fRowTotal")
    var fRowTotal: Double,
    @SerializedName("fTaxAmount")
    var fTaxAmount: Double,
    @SerializedName("itemDiscount")
    var itemDiscount: Double,
    @SerializedName("lPrice")
    var lPrice: Double,
    @SerializedName("lineTotal")
    var lineTotal: Double,
    @SerializedName("minimumSellingPrice")
    var minimumSellingPrice: Double,
    @SerializedName("netTotal")
    var netTotal: Double,
    @SerializedName("pcsPerCarton")
    var pcsPerCarton: Double,
    @SerializedName("piecePrice")
    var piecePrice: Double,
    @SerializedName("price")
    var price: Double,
    @SerializedName("productCode")
    var productCode: String,
    @SerializedName("productName")
    var productName: String,
    @SerializedName("purchaseTaxCode")
    var purchaseTaxCode: String,
    @SerializedName("purchaseTaxPerc")
    var purchaseTaxPerc: String,
    @SerializedName("purchaseTaxRate")
    var purchaseTaxRate: String,
    @SerializedName("quantity")
    var quantity: Double,
    @SerializedName("retailPrice")
    var retailPrice: Double,
    @SerializedName("salesEmployeeCode")
    var salesEmployeeCode: String,
    @SerializedName("slNo")
    var slNo: String,
    @SerializedName("soDate")
    var soDate: String,
    @SerializedName("soNo")
    var soNo: String,
    @SerializedName("stockCommited")
    var stockCommited: Double,
    @SerializedName("stockInHand")
    var stockInHand: Double,
    @SerializedName("subTotal")
    var subTotal: Double,
    @SerializedName("taxAmount")
    var taxAmount: Double,
    @SerializedName("taxCode")
    var taxCode: String,
    @SerializedName("taxPerc")
    var taxPerc: String,
    @SerializedName("taxRate")
    var taxRate: Double,
    @SerializedName("taxStatus")
    var taxStatus: String,
    @SerializedName("taxType")
    var taxType: String,
    @SerializedName("remarks")
    var remarks: String,
    @SerializedName("palletRemarks")
    var palletRemarks: String,
    @SerializedName("total")
    var total: Double,
    @SerializedName("totalTax")
    var totalTax: Double,
    @SerializedName("unitPrice")
    var unitPrice: Double,
    @SerializedName("unitQty")
    var unitQty: Double,
    @SerializedName("uoMCode")
    var uoMCode: Any,
    @SerializedName("uoMName")
    var uoMName: Any,
    @SerializedName("uomCode")
    var uomCode: Any,
    @SerializedName("updateDate")
    var updateDate: String,
    @SerializedName("warehouseCode")
    var warehouseCode: String,
    @SerializedName("pickedQty")
    var pickedQuantity: Double,
    @SerializedName("releasedQty")
    var releasedQty: Double,
    @SerializedName("balance")
    var balance: Int,
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
    @SerializedName("lineNum")
    var lineNum: Int?,

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