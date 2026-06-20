package com.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("sucesso")  val sucesso: Boolean,
    @SerializedName("usuario")  val usuario: String,
    @SerializedName("token")    val token: Int,
    @SerializedName("mensagem") val mensagem: String? = null
)