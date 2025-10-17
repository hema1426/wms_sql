package com.winapp.KHDelivery.model.newPickDetail


import com.google.gson.annotations.SerializedName

data class NewPickListDetailResponse(
    @SerializedName("responseData")
    var responseData: ArrayList<NewpickDetailRespData>,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("statusMessage")
    var statusMessage: String
)