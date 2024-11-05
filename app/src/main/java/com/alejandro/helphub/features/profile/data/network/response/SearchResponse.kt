package com.alejandro.helphub.features.profile.data.network.response

import com.google.gson.annotations.SerializedName

data class SearchResponse (
    @SerializedName("_id") val id:String,
    @SerializedName("email") val email:String,
    @SerializedName("nameUser") val nameUser:String,
    @SerializedName("phone") val phone:String,
    @SerializedName("optionCall") val optionCall:String,
    @SerializedName("showPhone") val showPhone:String,
    @SerializedName("blocked") val blocked:String
    )