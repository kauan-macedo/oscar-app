package com.seugrupo.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class VotoRequest(
    @SerializedName("login")      val login: String,
    @SerializedName("token")      val token: Int,
    @SerializedName("id_filme")   val idFilme: String,
    @SerializedName("id_diretor") val idDiretor: String
)
 
