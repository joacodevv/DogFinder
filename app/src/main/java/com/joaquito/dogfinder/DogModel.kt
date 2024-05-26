package com.joaquito.dogfinder

import com.google.gson.annotations.SerializedName

data class DogModel(
    @SerializedName("status") val status:String,
    @SerializedName("message") val images:List<String>)