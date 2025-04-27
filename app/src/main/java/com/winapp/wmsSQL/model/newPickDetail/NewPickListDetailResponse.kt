package com.winapp.wmsSQL.model.newPickDetail


import com.google.gson.annotations.SerializedName
import com.winapp.wmsSQL.model.newPickDetail.NewpickDetailRespData

data class NewPickListDetailResponse(
    @SerializedName("responseData")
    var responseData: ArrayList<NewpickDetailRespData>,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("statusMessage")
    var statusMessage: String
)