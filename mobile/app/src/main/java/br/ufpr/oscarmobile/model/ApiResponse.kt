package com.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName
 
data class ApiResponse(
    @SerializedName("sucesso")  val sucesso: Boolean,
    @SerializedName("mensagem") val mensagem: String? = null
)
