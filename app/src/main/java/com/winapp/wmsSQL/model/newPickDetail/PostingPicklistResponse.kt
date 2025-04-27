package com.winapp.wmsSQL.model.newPickDetail


import com.google.gson.annotations.SerializedName

data class PostingPicklistResponse(
   // @SerializedName("responseData")
    //var responseData: ResponseDataPickPostResponse,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("statusMessage")
    var statusMessage: String
)